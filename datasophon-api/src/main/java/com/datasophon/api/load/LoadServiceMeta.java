package com.datasophon.api.load;

import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.configuration.ConfigBean;
import com.datasophon.api.master.HostActor;
import com.datasophon.api.service.*;
import com.datasophon.common.model.*;
import com.datasophon.dao.entity.*;
import com.datasophon.api.service.*;
import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.utils.HostUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LoadServiceMeta implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LoadServiceMeta.class);

    private static final String path = "meta";

    @Autowired
    private FrameServiceService frameServiceService;

    @Autowired
    private FrameInfoService frameInfoService;

    @Autowired
    private FrameServiceRoleService roleService;

    @Autowired
    private ClusterVariableService variableService;

    @Autowired
    private ClusterInfoService clusterInfoService;

    @Autowired
    private ConfigBean configBean;

    @Autowired
    private ClusterServiceInstanceService serviceInstanceService;

    @Autowired
    private ClusterServiceInstanceConfigService serviceInstanceConfigService;

    @Autowired
    private ClusterServiceInstanceRoleGroupService roleGroupService;

    @Autowired
    private ClusterServiceRoleGroupConfigService roleGroupConfigService;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        //加载hosts文件，获取ip与hostname对面的map
        HostUtils.read();
        logger.info("put ip host map into cache");
        //加载服务配置，服务角色信息，服务角色配置，服务角色启停脚本信息
        File[] ddps = FileUtil.ls(path);
        //加载全局变量
        List<ClusterInfoEntity> clusters = clusterInfoService.list();
        if (Objects.nonNull(clusters) && clusters.size() > 0) {
            for (ClusterInfoEntity cluster : clusters) {
                HashMap<String, String> globalVariables = new HashMap<>();
                List<ClusterVariable> variables = variableService.list(new QueryWrapper<ClusterVariable>().eq(Constants.CLUSTER_ID, cluster.getId()));
                for (ClusterVariable variable : variables) {
                    globalVariables.put(variable.getVariableName(), variable.getVariableValue());
                }
                globalVariables.put("${installPath}", Constants.INSTALL_PATH);
                globalVariables.put("${apiHost}", InetAddress.getLocalHost().getHostName());
                globalVariables.put("${apiPort}", configBean.getServerPort());
                globalVariables.put("${HADOOP_HOME}", Constants.INSTALL_PATH + "/hadoop-3.3.3");
                logger.info(globalVariables.get("${apiHost}"));
                CacheUtils.put("globalVariables" + Constants.UNDERLINE + cluster.getId(), globalVariables);
                ProcessUtils.createServiceActor(cluster);
            }
        }

        HashMap<String, String> jmxMap = new HashMap<>();
        for (File path : ddps) {
            List<File> files = FileUtil.loopFiles(path);
            String frameCode = path.getName();
            FrameInfoEntity frameInfo = frameInfoService.getOne(new QueryWrapper<FrameInfoEntity>().eq("frame_code", frameCode));
            if (Objects.isNull(frameInfo)) {
                frameInfo = new FrameInfoEntity();
                frameInfo.setFrameCode(frameCode);
                frameInfoService.save(frameInfo);
            }
            //解析file,得到各服务的元信息
            for (File file : files) {
                if (file.getName().endsWith(Constants.JSON)) {

                    String serviceName = file.getParentFile().getName();
                    String serviceDdl = FileReader.create(file).readString();
                    ServiceInfo serviceInfo = JSONObject.parseObject(serviceDdl, ServiceInfo.class);
                    String serviceInfoMd5 = SecureUtil.md5(serviceDdl);

                    //保存服务配置
                    List<ServiceConfig> allParameters = serviceInfo.getParameters();
                    Map<String, ServiceConfig> map = allParameters.stream().collect(Collectors.toMap(ServiceConfig::getName, serviceConfig -> serviceConfig, (v1, v2) -> v1));
                    Map<Generators, List<ServiceConfig>> cofigMap = new HashMap<>();

                    ConfigWriter configWriter = serviceInfo.getConfigWriter();
                    List<Generators> generators = configWriter.getGenerators();
                    for (Generators generator : generators) {
                        List<ServiceConfig> list = new ArrayList<>();
                        List<String> includeParams = generator.getIncludeParams();
                        for (String includeParam : includeParams) {
                            if (map.containsKey(includeParam)) {
                                ServiceConfig serviceConfig = map.get(includeParam);
                                list.add(serviceConfig);
                            }
                        }
                        if (cofigMap.containsKey(generator)) {
                            cofigMap.get(generator).addAll(list);
                        } else {
                            cofigMap.put(generator, list);
                        }
                    }
                    //服务信息及服务配置信息持久化到数据库
                    FrameServiceEntity serviceEntity = frameServiceService.getServiceByFrameIdAndServiceName(frameInfo.getId(), serviceName);
                    if (Objects.isNull(serviceEntity)) {
                        serviceEntity = new FrameServiceEntity();
                        generateFrameService(frameCode, frameInfo, serviceName, serviceDdl, serviceInfo, serviceInfoMd5, serviceEntity, cofigMap, serviceInfo.getDecompressPackageName());

                        frameServiceService.save(serviceEntity);
                    } else if (!serviceEntity.getServiceJsonMd5().equals(serviceInfoMd5)) {
                        String configMapStr = JSONObject.toJSONString(cofigMap);
                        String cofigMapStrMd5 = SecureUtil.md5(configMapStr);
                        if (!cofigMapStrMd5.equals(serviceEntity.getConfigFileJsonMd5())) {
                            //更新服务实例配置文件
                            updateServiceInstanceConfig(frameCode, serviceInfo.getName(), serviceInfo.getParameters(), cofigMap);
                        }
                        generateFrameService(frameCode, frameInfo, serviceName, serviceDdl, serviceInfo, serviceInfoMd5, serviceEntity, cofigMap, serviceInfo.getDecompressPackageName());
                        frameServiceService.updateById(serviceEntity);
                    }
                    logger.info("put {} {} service config into cache", frameCode, serviceName);
                    CacheUtils.put(frameCode + Constants.UNDERLINE + serviceInfo.getName() + Constants.CONFIG, allParameters);
                    logger.info("put {} {} config file map into cache", frameCode, serviceName);
                    CacheUtils.put(frameCode + Constants.UNDERLINE + serviceInfo.getName() + Constants.CONFIG_FILE, cofigMap);
                    //保存服务角色元信息
                    List<ServiceRoleInfo> serviceRoles = serviceInfo.getRoles();

                    for (ServiceRoleInfo serviceRole : serviceRoles) {
                        String key = frameCode + Constants.UNDERLINE + serviceInfo.getName() + Constants.UNDERLINE + serviceRole.getName();
                        logger.info("put {} {} {} service role info into cache", frameCode, serviceName, serviceRole.getName());
                        String jmxKey = frameCode + Constants.UNDERLINE + Constants.SERVICE_ROLE_JMX_MAP;
                        if (StringUtils.isNotBlank(serviceRole.getJmxPort())) {
                            jmxMap.put(serviceRole.getName(), serviceRole.getJmxPort());
                        }
                        CacheUtils.put(jmxKey, jmxMap);
                        CacheUtils.put(key, serviceRole);
                        String serviceRoleJson = JSONObject.toJSONString(serviceRole);
                        String serviceRoleJsonMd5 = SecureUtil.md5(serviceRoleJson);
                        //持久化服务角色元信息至数据库
                        FrameServiceRoleEntity role = roleService.getServiceRoleByServiceIdAndServiceRoleName(serviceEntity.getId(), serviceRole.getName());
                        if (Objects.isNull(role)) {
                            role = new FrameServiceRoleEntity();
                            generateFrameServiceRole(frameCode, serviceEntity, serviceRole, serviceRoleJson, serviceRoleJsonMd5, role);
                            roleService.save(role);
                        } else if (!role.getServiceRoleJsonMd5().equals(serviceRoleJsonMd5)) {
                            generateFrameServiceRole(frameCode, serviceEntity, serviceRole, serviceRoleJson, serviceRoleJsonMd5, role);
                            roleService.updateById(role);
                        }
                    }
                    logger.info("put {} {} service info into cache", frameCode, serviceName);
                    CacheUtils.put(frameCode + Constants.UNDERLINE + serviceName, serviceInfo);
                }
            }
        }
    }

    private void updateServiceInstanceConfig(String frameCode, String serviceName, List<ServiceConfig> parameters, Map<Generators, List<ServiceConfig>> cofigMap) {
        //查询frameCode相同的集群
        List<ClusterInfoEntity> clusters = clusterInfoService.getClusterByFrameCode(frameCode);
        //查询集群的服务实例
        for (ClusterInfoEntity cluster : clusters) {
            ClusterServiceInstanceEntity serviceInstance = serviceInstanceService.getServiceInstanceByClusterIdAndServiceName(cluster.getId(), serviceName);
            if (Objects.nonNull(serviceInstance)) {
                ClusterServiceRoleGroupConfig config = roleGroupService.getRoleGroupConfigByServiceId(serviceInstance.getId());
                String configJson = config.getConfigJson();
                List<ServiceConfig> serviceConfigs = JSONArray.parseArray(configJson, ServiceConfig.class);
                ProcessUtils.addAll(serviceConfigs, parameters);
                //更新服务实例的配置
                config.setConfigJson(JSONObject.toJSONString(serviceConfigs));
                roleGroupConfigService.updateById(config);
            }
        }

    }

    private void generateFrameServiceRole(String frameCode, FrameServiceEntity serviceEntity, ServiceRoleInfo serviceRole, String serviceRoleJson, String serviceRoleJsonMd5, FrameServiceRoleEntity role) {
        role.setServiceId(serviceEntity.getId());
        role.setServiceRoleName(serviceRole.getName());
        role.setCardinality(serviceRole.getCardinality());
        role.setFrameCode(frameCode);
        role.setServiceRoleJson(serviceRoleJson);
        role.setServiceRoleType(CommonUtils.convertRoleType(serviceRole.getRoleType().getName()));
        role.setJmxPort(serviceRole.getJmxPort());
        role.setServiceRoleJsonMd5(serviceRoleJsonMd5);
        role.setLogFile(serviceRole.getLogFile());
    }

    private void generateFrameService(String frameCode, FrameInfoEntity frameInfo, String serviceName, String serviceDdl, ServiceInfo serviceInfo, String serviceInfoMd5, FrameServiceEntity serviceEntity, Map<Generators, List<ServiceConfig>> configFileMap, String decompressPackageName) {
        serviceEntity.setServiceName(serviceName);
        serviceEntity.setLabel(serviceInfo.getLabel());
        serviceEntity.setFrameId(frameInfo.getId());
        serviceEntity.setServiceDesc(serviceInfo.getDescription());
        serviceEntity.setServiceVersion(serviceInfo.getVersion());
        serviceEntity.setPackageName(serviceInfo.getPackageName());
        serviceEntity.setDependencies(StringUtils.join(serviceInfo.getDependencies(), ","));
        serviceEntity.setFrameCode(frameCode);
        serviceEntity.setServiceConfig(JSON.toJSONString(serviceInfo.getParameters()));
        serviceEntity.setServiceJson(serviceDdl);
        serviceEntity.setServiceJsonMd5(serviceInfoMd5);
        serviceEntity.setDecompressPackageName(decompressPackageName);
        serviceEntity.setConfigFileJson(JSONObject.toJSONString(configFileMap));
        serviceEntity.setConfigFileJsonMd5(SecureUtil.md5(serviceEntity.getConfigFileJson()));
        serviceEntity.setSortNum(serviceInfo.getSortNum());
    }

}
