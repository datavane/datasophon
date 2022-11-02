package com.datasophon.api.master;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.SubmitActiveTaskNodeCommand;
import com.datasophon.common.enums.ServiceExecuteState;
import com.datasophon.common.enums.ServiceRoleType;
import com.datasophon.common.model.DAGGraph;
import com.datasophon.common.model.ServiceExecuteResultMessage;
import com.datasophon.common.model.ServiceNode;
import com.datasophon.common.model.ServiceRoleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceExecuteResultActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceExecuteResultActor.class);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ServiceExecuteResultMessage) {
            ServiceExecuteResultMessage result = (ServiceExecuteResultMessage) message;

            DAGGraph<String, ServiceNode, String> dag = result.getDag();
            Map<String, ServiceExecuteState> activeTaskList = result.getActiveTaskList();
            Map<String, String> errorTaskList = result.getErrorTaskList();
            Map<String, String> readyToSubmitTaskList = result.getReadyToSubmitTaskList();
            Map<String, String> completeTaskList = result.getCompleteTaskList();
            ActorSystem system = (ActorSystem) CacheUtils.get("actorSystem");
            ActorRef submitTaskNodeActor = (ActorRef) CacheUtils.get("submitTaskNodeActor");
            String node = result.getServiceName();
            ServiceNode servicNode = dag.getNode(node);
            if (result.getServiceRoleType().equals(ServiceRoleType.MASTER)) {
                if (result.getServiceExecuteState().equals(ServiceExecuteState.ERROR)) {
                    //该节点master角色操作失败，移动到error列表
                    errorTaskList.put(node, "");
                    activeTaskList.remove(node);
                    readyToSubmitTaskList.remove(node);
                    completeTaskList.put(node, "");
                    //更改指令执行状态，依赖该节点的下游服务指令状态改为取消
                    logger.info("{} master roles failed , cancel all next node by hostCommandId {}",node,servicNode.getMasterRoles().get(0).getHostCommandId());
                    String hostCommandId = servicNode.getMasterRoles().get(0).getHostCommandId();
                    ProcessUtils.updateCommandStateToFailed( hostCommandId);
                } else if (result.getServiceExecuteState().equals(ServiceExecuteState.SUCCESS)) {
                    //该节点master角色指令执行完毕，开始执行worker节点操作
                    ServiceNode serviceNode = dag.getNode(node);
                    List<ServiceRoleInfo> elseRoles = serviceNode.getElseRoles();
                    if (elseRoles.size() > 0) {
                        logger.info("start to submit worker/client roles");
                        ActorSelection serviceActor = system.actorSelection("/user/"+result.getClusterCode()+"-serviceActor-" + node);
                        ProcessUtils.buildExecuteServiceRoleCommand(result.getClusterId(),result.getCommandType(),result.getClusterCode(), dag, activeTaskList, errorTaskList, readyToSubmitTaskList, completeTaskList, node, elseRoles, serviceActor, ServiceRoleType.WORKER);
                    } else {
                        activeTaskList.remove(node);
                        readyToSubmitTaskList.remove(node);
                        //提交下一个节点
                        logger.info("start to submit next node");
                        tellToSubmitActiveTaskNode(result, dag, activeTaskList, errorTaskList, readyToSubmitTaskList, completeTaskList, submitTaskNodeActor,node);
                    }
                }
            }
            if (result.getServiceRoleType().equals(ServiceRoleType.WORKER)) {
                if (ServiceExecuteState.SUCCESS.equals(result.getServiceExecuteState())) {
                    activeTaskList.remove(node);
                    readyToSubmitTaskList.remove(node);
                    completeTaskList.put(node, "");
                    //master与worker都安装完成执行下一节点
                    logger.info("master and worker node all submit success");
                    tellToSubmitActiveTaskNode(result, dag, activeTaskList, errorTaskList, readyToSubmitTaskList, completeTaskList, submitTaskNodeActor,node);
                } else if (ServiceExecuteState.ERROR.equals(result.getServiceExecuteState())) {
                    errorTaskList.put(node, "");
                    activeTaskList.remove(node);
                    readyToSubmitTaskList.remove(node);
                    completeTaskList.put(node, "");
                    //更改指令执行状态，依赖该节点的下游服务指令状态改为取消
                    logger.info("{} worker roles failed , cancel all next node by hostCommandId {}",node,servicNode.getElseRoles().get(0).getHostCommandId());
                    String hostCommandId = servicNode.getElseRoles().get(0).getHostCommandId();
                    ProcessUtils.updateCommandStateToFailed( hostCommandId);
                }
            }
        } else {
            unhandled(message);
        }
    }

    private void tellToSubmitActiveTaskNode(ServiceExecuteResultMessage result,
                                            DAGGraph<String, ServiceNode, String> dag,
                                            Map<String, ServiceExecuteState> activeTaskList,
                                            Map<String, String> errorTaskList,
                                            Map<String, String> readyToSubmitTaskList,
                                            Map<String, String> completeTaskList,
                                            ActorRef submitTaskNodeActor,
                                            String node) {
        Set<String> subsequentNodes = dag.getSubsequentNodes(node);
        logger.info("the subsequent nodes is {}", subsequentNodes.toString());
        for (String subsequentNode : subsequentNodes) {
            readyToSubmitTaskList.put(subsequentNode, "");
        }
        SubmitActiveTaskNodeCommand submitActiveTaskNodeCommand = new SubmitActiveTaskNodeCommand();
        submitActiveTaskNodeCommand.setCommandType(result.getCommandType());
        submitActiveTaskNodeCommand.setDag(dag);
        submitActiveTaskNodeCommand.setClusterId(result.getClusterId());
        submitActiveTaskNodeCommand.setActiveTaskList(activeTaskList);
        submitActiveTaskNodeCommand.setErrorTaskList(errorTaskList);
        submitActiveTaskNodeCommand.setReadyToSubmitTaskList(readyToSubmitTaskList);
        submitActiveTaskNodeCommand.setCompleteTaskList(completeTaskList);

        submitActiveTaskNodeCommand.setClusterCode(result.getClusterCode());

        submitTaskNodeActor.tell(submitActiveTaskNodeCommand, getSelf());
    }


}
