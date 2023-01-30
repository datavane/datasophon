package com.datasophon.api.master;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.service.*;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.dao.entity.*;
import com.datasophon.api.service.*;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.StartExecuteCommandCommand;
import com.datasophon.common.command.SubmitActiveTaskNodeCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.enums.ServiceExecuteState;
import com.datasophon.common.enums.ServiceRoleType;
import com.datasophon.common.model.DAGGraph;
import com.datasophon.common.model.ServiceNode;
import com.datasophon.common.model.ServiceRoleInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DAGBuildActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(DAGBuildActor.class);


    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StartExecuteCommandCommand) {
            DAGGraph<String, ServiceNode, String> dag = new DAGGraph<>();

            StartExecuteCommandCommand executeCommandCommand = (StartExecuteCommandCommand) message;
            CommandType commandType = executeCommandCommand.getCommandType();
            logger.info("start execute command");

            ClusterServiceCommandService commandService = SpringTool.getApplicationContext().getBean(ClusterServiceCommandService.class);
            ClusterServiceCommandHostCommandService hostCommandService = SpringTool.getApplicationContext().getBean(ClusterServiceCommandHostCommandService.class);
            FrameServiceRoleService frameServiceRoleService = SpringTool.getApplicationContext().getBean(FrameServiceRoleService.class);
            FrameServiceService frameService = SpringTool.getApplicationContext().getBean(FrameServiceService.class);
            ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);

            ClusterInfoEntity clusterInfo = clusterInfoService.getById(executeCommandCommand.getClusterId());
            List<ClusterServiceCommandEntity> list = commandService.list(new QueryWrapper<ClusterServiceCommandEntity>().in(Constants.COMMAND_ID, executeCommandCommand.getCommandIds()));

            Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+executeCommandCommand.getClusterId());

            ArrayList<FrameServiceEntity> frameServiceList = new ArrayList<>();
            if (Objects.nonNull(list) && list.size() > 0) {
                for (ClusterServiceCommandEntity command : list) {
                    //build dag
                    List<ClusterServiceCommandHostCommandEntity> hostCommandList = hostCommandService.getHostCommandListByCommandId(command.getCommandId());
                    List<ServiceRoleInfo> masterRoles = new ArrayList<>();
                    List<ServiceRoleInfo> elseRoles = new ArrayList<>();
                    ServiceNode serviceNode = new ServiceNode();
                    FrameServiceEntity serviceEntity = frameService.getServiceByFrameCodeAndServiceName(clusterInfo.getClusterFrame(), command.getServiceName());
                    frameServiceList.add(serviceEntity);
                    serviceNode.setCommandId(command.getCommandId());
                    for (ClusterServiceCommandHostCommandEntity hostCommand : hostCommandList) {
                        logger.info("service role is {}",hostCommand.getServiceRoleName());
                        FrameServiceRoleEntity serviceRole = frameServiceRoleService.getServiceRoleByFrameCodeAndServiceRoleName(clusterInfo.getClusterFrame(), hostCommand.getServiceRoleName());
                        ServiceRoleInfo serviceRoleInfo = JSONObject.parseObject(serviceRole.getServiceRoleJson(), ServiceRoleInfo.class);
                        serviceRoleInfo.setHostname(hostCommand.getHostname());
                        serviceRoleInfo.setHostCommandId(hostCommand.getHostCommandId());
                        serviceRoleInfo.setClusterId(clusterInfo.getId());
                        serviceRoleInfo.setParentName(command.getServiceName());
                        serviceRoleInfo.setPackageName(serviceEntity.getPackageName());
                        serviceRoleInfo.setDecompressPackageName(serviceEntity.getDecompressPackageName());
                        serviceRoleInfo.setCommandType(commandType);
                        serviceRoleInfo.setServiceInstanceId(command.getServiceInstanceId());
                        if("NameNode".equals(serviceRoleInfo.getName()) && hostCommand.getHostname().equals(globalVariables.get("${nn2}"))){
                            logger.info("set to slave namenode");
                            serviceRoleInfo.setSlave(true);
                            serviceRoleInfo.setSortNum(5);
                        }
                        if("ZKFC".equals(serviceRoleInfo.getName()) && hostCommand.getHostname().equals(globalVariables.get("${ZKFC2}"))){
                            logger.info("set to slave zkfc");
                            serviceRoleInfo.setSlave(true);
                            serviceRoleInfo.setSortNum(6);
                        }
                        if("HiveServer2".equals(serviceRoleInfo.getName()) && globalVariables.containsKey("${masterHiveServer2}") && !hostCommand.getHostname().equals(globalVariables.get("${masterHiveServer2}"))){
                            logger.info("set to slave hiveserver2");
                            serviceRoleInfo.setSlave(true);
                        }
                        if("SRFE".equals(serviceRoleInfo.getName()) || "DorisFE".equals(serviceRoleInfo.getName())){
                            String feMaster = globalVariables.get("${feMaster}");
                            if(hostCommand.getHostname().equals(feMaster)){
                                logger.info("fe master is {}",feMaster);
                                serviceRoleInfo.setSortNum(1);
                            }else{
                                logger.info("set fe follower master");
                                serviceRoleInfo.setMasterHost(feMaster);
                                serviceRoleInfo.setSlave(true);
                                serviceRoleInfo.setSortNum(2);
                            }
                        }
                        if("SRBE".equals(serviceRoleInfo.getName()) || "DorisBe".equals(serviceRoleInfo.getName())){
                            String feMaster = globalVariables.get("${feMaster}");
                            logger.info("fe master is {}",feMaster);
                            serviceRoleInfo.setMasterHost(feMaster);
                        }
                        if (ServiceRoleType.MASTER.equals(serviceRoleInfo.getRoleType())) {
                            masterRoles.add(serviceRoleInfo);
                        } else {
                            elseRoles.add(serviceRoleInfo);
                        }
                    }
                    serviceNode.setMasterRoles(masterRoles);
                    serviceNode.setElseRoles(elseRoles);
                    dag.addNode(command.getServiceName(), serviceNode);
                }
                //build edge
                for (FrameServiceEntity serviceEntity : frameServiceList) {
                    if (StringUtils.isNotBlank(serviceEntity.getDependencies())) {
                        for (String dependency : serviceEntity.getDependencies().split(",")) {
                            if(dag.containsNode(dependency)){
                                dag.addEdge(dependency, serviceEntity.getServiceName(), false);
                            }
                        }
                    }
                }
            }
            if(commandType == CommandType.STOP_SERVICE){
                logger.info("reverse dag");
                DAGGraph<String, ServiceNode, String> reverseDagGraph = dag.getReverseDagGraph(dag);
                dag = reverseDagGraph;
            }
            Map<String, String> errorTaskList = new ConcurrentHashMap<>();
            Map<String, ServiceExecuteState> activeTaskList = new ConcurrentHashMap<>();
            Map<String, String>  readyToSubmitTaskList = new ConcurrentHashMap<>();
            Map<String, String>  completeTaskList = new ConcurrentHashMap<>();
            Collection<String> beginNode = dag.getBeginNode();
            logger.info("beginNode is {}",beginNode.toString());
            for (String node : beginNode) {
                readyToSubmitTaskList.put(node,"");
            }
            SubmitActiveTaskNodeCommand submitActiveTaskNodeCommand = new SubmitActiveTaskNodeCommand();
            submitActiveTaskNodeCommand.setCommandType(executeCommandCommand.getCommandType());
            submitActiveTaskNodeCommand.setDag(dag);
            submitActiveTaskNodeCommand.setClusterId(clusterInfo.getId());
            submitActiveTaskNodeCommand.setActiveTaskList(activeTaskList);
            submitActiveTaskNodeCommand.setErrorTaskList(errorTaskList);
            submitActiveTaskNodeCommand.setReadyToSubmitTaskList(readyToSubmitTaskList);
            submitActiveTaskNodeCommand.setCompleteTaskList(completeTaskList);
            submitActiveTaskNodeCommand.setClusterCode(clusterInfo.getClusterCode());
            ActorRef submitTaskNodeActor = ActorUtils.getLocalActor(SubmitTaskNodeActor.class,ActorUtils.getActorRefName(SubmitTaskNodeActor.class));
            submitTaskNodeActor.tell(submitActiveTaskNodeCommand, getSelf());
        }
    }
}
