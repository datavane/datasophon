package com.datasophon.api.master;


import akka.actor.UntypedActor;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasophon.api.master.handler.service.*;
import com.datasophon.api.service.ClusterServiceInstanceService;
import com.datasophon.api.service.ClusterServiceRoleGroupConfigService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.command.ExecuteServiceRoleCommand;
import com.datasophon.common.model.Generators;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.enums.ServiceExecuteState;
import com.datasophon.common.enums.ServiceRoleType;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.dao.entity.ClusterServiceRoleGroupConfig;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.NeedRestart;
import com.datasophon.dao.enums.ServiceRoleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ServiceActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceActor.class);

    @Override
    public  void postStop(){

        logger.info("{} service actor stopped ",getSelf().path().toString());
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof ExecuteServiceRoleCommand) {
            ExecuteServiceRoleCommand executeServiceRoleCommand = (ExecuteServiceRoleCommand) message;
            ClusterServiceRoleGroupConfigService roleGroupConfigService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleGroupConfigService.class);
            List<ServiceRoleInfo> serviceRoleInfoList = executeServiceRoleCommand.getServiceRoles();
            ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
            Collections.sort(serviceRoleInfoList);
            Integer successNum = 0;
            int id = 1;
            for (ServiceRoleInfo serviceRoleInfo : serviceRoleInfoList) {
                logger.info("{} service role size is {}",serviceRoleInfo.getName(),serviceRoleInfoList.size());
                ExecResult execResult = new ExecResult();
                Integer serviceInstanceId = serviceRoleInfo.getServiceInstanceId();
//                ClusterServiceInstanceEntity serviceInstanceEntity = serviceInstanceService.getById(serviceInstanceId);
                ClusterServiceRoleInstanceEntity serviceRoleInstance = roleInstanceService.getOneServiceRole(serviceRoleInfo.getName(), serviceRoleInfo.getHostname(), serviceRoleInfo.getClusterId());
                HashMap<Generators, List<ServiceConfig>> configFileMap = new HashMap<>();
                boolean enableRangerPlugin = false;
                boolean needReConfig = false;
                if(executeServiceRoleCommand.getCommandType() == CommandType.INSTALL_SERVICE){
//                    ClusterServiceInstanceRoleGroup roleGroup = roleGroupService.getRoleGroupByServiceInstanceId(serviceInstanceId);
                    Integer roleGroupId = (Integer) CacheUtils.get("UseRoleGroup_" + serviceInstanceId);
                    ClusterServiceRoleGroupConfig config = roleGroupConfigService.getConfigByRoleGroupId(roleGroupId);
                    enableRangerPlugin = generateConfigFileMap(configFileMap, enableRangerPlugin, config);
                }else if(serviceRoleInstance.getNeedRestart() == NeedRestart.YES){
                    ClusterServiceRoleGroupConfig config = roleGroupConfigService.getConfigByRoleGroupId(serviceRoleInstance.getRoleGroupId());
                    enableRangerPlugin = generateConfigFileMap(configFileMap, enableRangerPlugin, config);
                    needReConfig = true;
                }
                logger.info("set enable ranger plugin {}",enableRangerPlugin);
                serviceRoleInfo.setConfigFileMap(configFileMap);
                serviceRoleInfo.setId(id);
                serviceRoleInfo.setEnableRangerPlugin(enableRangerPlugin);
                id++;
                switch (executeServiceRoleCommand.getCommandType()) {
                    case INSTALL_SERVICE:
                        //安装，配置，启动三步
                        try {
                            logger.info("start to install {} int host {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                            ServiceHandler serviceInstallHandler = new ServiceInstallHandler();
                            ServiceHandler serviceConfigureHandler = new ServiceConfigureHandler();
                            ServiceHandler serviceStartHandler = new ServiceStartHandler();
                            serviceInstallHandler.setNext(serviceConfigureHandler);
                            serviceConfigureHandler.setNext(serviceStartHandler);

                            execResult = serviceInstallHandler.handlerRequest(serviceRoleInfo);
                            if (Objects.nonNull(execResult) && execResult.getExecResult()) {
                                //部署成功，数据持久化
                                ProcessUtils.saveServiceInstallInfo(serviceRoleInfo);
                                successNum += 1;
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType()) && successNum == serviceRoleInfoList.size()) {
                                    logger.info("all master role has installed");
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                                } else if (!ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType()) && successNum > 0){
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                                }
                                logger.info("{} install success in {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                            } else {
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                    logger.info("{} install failed in {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                                }
                            }

                        } catch (Exception e) {
                            logger.info("{} install failed in {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                            ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                            e.printStackTrace();
                        }
                        break;
                    case START_SERVICE:
                        try {
                            logger.info("start  {} in host {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                            if(needReConfig){
                                ServiceConfigureHandler serviceHandler = new ServiceConfigureHandler();
                                ServiceHandler serviceStartHandler = new ServiceStartHandler();
                                serviceHandler.setNext(serviceStartHandler);
                                execResult = serviceHandler.handlerRequest(serviceRoleInfo);
                            }else{
                                ServiceHandler serviceStartHandler = new ServiceStartHandler();
                                execResult = serviceStartHandler.handlerRequest(serviceRoleInfo);
                            }
                            if (Objects.nonNull(execResult) && execResult.getExecResult()) {
                                successNum += 1;
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType()) && successNum == serviceRoleInfoList.size()) {
                                    logger.info("{} start success", serviceRoleInfo.getParentName());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                                }
                                //更新角色实例状态为正在运行
                                ProcessUtils.updateServiceRoleState(CommandType.START_SERVICE,
                                                                    serviceRoleInfo.getName(),
                                                                    serviceRoleInfo.getHostname(),
                                                                    executeServiceRoleCommand.getClusterId(),
                                                                    ServiceRoleState.RUNNING);
                            } else {
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                    logger.info("{} start failed", serviceRoleInfo.getParentName());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                                }
                            }
                            if (!ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                logger.info("{} worker/client end start", serviceRoleInfo.getParentName());
                                ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                            }
                        } catch (Exception e) {

                            ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                            e.printStackTrace();
                        }
                        break;
                    case STOP_SERVICE:
                        try {
                            logger.info("stop {} in host {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                            ServiceHandler serviceStopHandler = new ServiceStopHandler();
                            execResult = serviceStopHandler.handlerRequest(serviceRoleInfo);
                            if (Objects.nonNull(execResult) && execResult.getExecResult()) {//执行成功
                                successNum += 1;
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType()) && successNum == serviceRoleInfoList.size()) {
                                    logger.info("{} stop success",serviceRoleInfo.getParentName());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                                }
                                //更新角色实例状态为停止
                                ProcessUtils.updateServiceRoleState(CommandType.STOP_SERVICE,
                                        serviceRoleInfo.getName(),
                                        serviceRoleInfo.getHostname(),
                                        executeServiceRoleCommand.getClusterId(),
                                        ServiceRoleState.STOP);
                            } else {//执行失败
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                    logger.info("{} stop failed",serviceRoleInfo.getParentName());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                                }
                            }
                            if (!ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                logger.info("{} worker/client end stop", serviceRoleInfo.getParentName());
                                ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                            }
                        } catch (Exception e) {
                            ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                            e.printStackTrace();
                        }
                        break;
                    case RESTART_SERVICE:
                        try {
                            logger.info("restart {} in host {}", serviceRoleInfo.getName(), serviceRoleInfo.getHostname());
                            ServiceHandler serviceStopHandler = new ServiceStopHandler();

                            ServiceHandler serviceStartHandler = new ServiceStartHandler();
                            if(needReConfig){
                                ServiceConfigureHandler serviceConfigureHandler = new ServiceConfigureHandler();
                                serviceStopHandler.setNext(serviceConfigureHandler);
                                serviceConfigureHandler.setNext(serviceStartHandler);
                            }else{
                                serviceStopHandler.setNext(serviceStartHandler);
                            }
                            execResult = serviceStopHandler.handlerRequest(serviceRoleInfo);
                            if (Objects.nonNull(execResult) && execResult.getExecResult()) {

                                successNum += 1;
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType()) && successNum == serviceRoleInfoList.size()) {
                                    logger.info("{} restart success",serviceRoleInfo.getParentName());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                                }
                                //更新角色实例状态为正在运行
                                ProcessUtils.updateServiceRoleState(CommandType.RESTART_SERVICE,serviceRoleInfo.getName(), serviceRoleInfo.getHostname(), executeServiceRoleCommand.getClusterId(), ServiceRoleState.RUNNING);

                            } else {
                                if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                    logger.info("{} restart failed",serviceRoleInfo.getParentName());
                                    ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                                }
                            }
                            if (!ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                                logger.info("{} worker/client end restart", serviceRoleInfo.getParentName());
                                ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.SUCCESS);
                            }
                        } catch (Exception e) {
                            ProcessUtils.tellCommandActorResult(serviceRoleInfo.getParentName(), executeServiceRoleCommand, ServiceExecuteState.ERROR);
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                ProcessUtils.handleCommandResult(serviceRoleInfo.getHostCommandId(), execResult.getExecResult(), execResult.getExecOut());
            }
        } else {
            unhandled(message);
        }
    }
    //生成configFileMap
    private boolean generateConfigFileMap(HashMap<Generators, List<ServiceConfig>> configFileMap, boolean enableRangerPlugin, ClusterServiceRoleGroupConfig config) {
        Map<JSONObject, JSONArray> map = JSONObject.parseObject(config.getConfigFileJson(), Map.class);
        for (JSONObject fileJson : map.keySet()) {
            Generators generators = fileJson.toJavaObject(Generators.class);
            List<ServiceConfig> serviceConfigs = map.get(fileJson).toJavaList(ServiceConfig.class);
            //判断是否配置了ranger plugin
            if ("install.properties".equals(generators.getFilename()) && serviceConfigs.size() > 0) {
                if (serviceConfigs.get(0).isRequired()) {
                    enableRangerPlugin = true;
                }
            }
            configFileMap.put(generators, serviceConfigs);
        }
        return enableRangerPlugin;
    }

}
