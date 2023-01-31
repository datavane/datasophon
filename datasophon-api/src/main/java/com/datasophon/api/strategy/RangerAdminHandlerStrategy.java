package com.datasophon.api.strategy;

import com.alibaba.fastjson.JSONObject;
import com.datasophon.api.service.*;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceInstanceEntity;
import com.datasophon.dao.entity.ClusterServiceRoleGroupConfig;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RangerAdminHandlerStrategy implements ServiceRoleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RangerAdminHandlerStrategy.class);

    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        if (hosts.size() == 1) {
            String rangerAdminUrl = "http://" + hosts.get(0) + ":6080";
            logger.info("rangerAdminUrl is {}", rangerAdminUrl);
            ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${rangerAdminUrl}", rangerAdminUrl);
        }
    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {

        //判断是否启用ranger插件
        for (ServiceConfig config : list) {
            //启用hdfs ranger插件，修改hdfs权限配置
            if ("enableHdfsPlugin".equals(config.getName()) && ((Boolean) config.getValue()).booleanValue()) {
                logger.info("enableHdfsPlugin");
                enableRangerPlugin(clusterId, "HDFS", "NameNode");
            }
            if ("enableHivePlugin".equals(config.getName()) && ((Boolean) config.getValue()).booleanValue()) {
                logger.info("enableHivePlugin");
                enableRangerPlugin(clusterId, "HIVE", "HiveServer2");
            }
            if ("enableHbasePlugin".equals(config.getName()) && ((Boolean) config.getValue()).booleanValue()) {
                logger.info("enableHivePlugin");
                enableRangerPlugin(clusterId, "HBASE", "HbaseMaster");
            }
        }
    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }

    private void enableRangerPlugin(Integer clusterId, String serviceName, String serviceRoleName) {
        ClusterServiceInstanceService serviceInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceInstanceService.class);
        ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
        ClusterServiceRoleGroupConfigService roleGroupConfigService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleGroupConfigService.class);
        ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);
        ServiceInstallService serviceInstallService = SpringTool.getApplicationContext().getBean(ServiceInstallService.class);
        ClusterInfoEntity clusterInfo = clusterInfoService.getById(clusterId);
        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+clusterId);
        ClusterServiceInstanceEntity serviceInstance = serviceInstanceService.getServiceInstanceByClusterIdAndServiceName(clusterId, serviceName);
        //查询角色组id
        List<ClusterServiceRoleInstanceEntity> roleList = roleInstanceService.getServiceRoleInstanceListByClusterIdAndRoleName(clusterId, serviceRoleName);

        if (Objects.nonNull(roleList) && roleList.size() > 0) {
            Integer roleGroupId = roleList.get(0).getRoleGroupId();

            ClusterServiceRoleGroupConfig config = roleGroupConfigService.getConfigByRoleGroupId(roleGroupId);
            List<ServiceConfig> serviceConfigs = JSONObject.parseArray(config.getConfigJson(), ServiceConfig.class);
            Map<String, ServiceConfig> map = serviceConfigs.stream().collect(Collectors.toMap(ServiceConfig::getName, serviceConfig -> serviceConfig, (v1, v2) -> v1));

            List<ServiceConfig> allParameters = (List<ServiceConfig>) CacheUtils.get(clusterInfo.getClusterFrame() + Constants.UNDERLINE + serviceInstance.getServiceName() + Constants.CONFIG);
            for (ServiceConfig parameter : allParameters) {
                String name = parameter.getName();
                if (map.containsKey(name)) {
                    parameter = map.get(name);
                }

                if ("permission".equals(parameter.getConfigType())) {
                    parameter.setHidden(false);
                    parameter.setRequired(true);
                }
                if ("dfs.permissions.enabled".equals(parameter.getName())) {
                    parameter.setHidden(false);
                    parameter.setRequired(true);
                    parameter.setValue(true);

                }
                if ("rangerAdminUrl".equals(parameter.getName())) {
                    parameter.setHidden(false);
                    parameter.setRequired(true);
                    parameter.setValue(globalVariables.get("${rangerAdminUrl}"));
                }
                if (!map.containsKey(name)) {
                    logger.info("put config {} into service {}", name, serviceRoleName);
                    serviceConfigs.add(parameter);
                }
            }
            serviceInstallService.saveServiceConfig(clusterId, serviceInstance.getServiceName(), serviceConfigs, roleGroupId);
        }
    }
}
