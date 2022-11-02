//package com.datasophon.ddh.worker.actor;
//
//import akka.actor.ActorRef;
//import akka.actor.ActorSelection;
//import akka.actor.UntypedActor;
//import cn.hutool.core.bean.BeanUtil;
//import com.datasophon.ddh.common.command.*;
//import ExecResult;
//import PropertyUtils;
//import com.datasophon.ddh.worker.handler.ServiceHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ServiceOperateActor extends UntypedActor {
//    private static final Logger logger = LoggerFactory.getLogger(ServiceOperateActor.class);
//
//    private ActorSelection masterActor;
//
//    public ServiceOperateActor() {
//        String masterHost = PropertyUtils.getString("masterHost");
//        this.masterActor = context().actorSelection("akka.tcp://ddh@" + masterHost + ":2551/user/master");
//    }
//
//    @Override
//    public void onReceive(Object message) throws Throwable, Throwable {
//        if (message instanceof InstallServiceRoleCommand) {
//            InstallServiceRoleCommand command = (InstallServiceRoleCommand) message;
//            ServiceHandler serviceHandler = new ServiceHandler();
//            ExecResult execResult = new ExecResult();
//            //下载安装包
//            logger.info("start install package {}",command.getPackageName());
//            execResult = serviceHandler.install(command.getPackageName(),command.getServiceName(),command.getPackageMd5());
//            if (execResult.getExecResult()) {
//
//                //生成配置文件
//                logger.info("start to generate config file");
//                execResult = serviceHandler.configure(command.getCofigFileMap(), command.getServiceName());
//                if (execResult.getExecResult()) {
//                    //执行启动脚本
//                    logger.info("start to start service role {}",command.getServiceRoleName());
//                    execResult = serviceHandler.start(command.getStartRunner(), command.getDecompressPackageName());
//                }
//            }
//            InstallServiceRoleCommandResult resultMessage = new InstallServiceRoleCommandResult();
//            resultMessage.setExecResult(execResult.getExecResult());
//            resultMessage.setExecOut(execResult.getExecOut());
//            resultMessage.setInstallServiceRoleCommand(command);
//            masterActor.tell(resultMessage,getSelf());
//            //启动定时任务状态监测
//            if(execResult.getExecResult()){
//                ActorSelection serviceCheckActor = context().actorSelection("/user/serviceCheck");
//                ServiceCheckCommand serviceCheckCommand = new ServiceCheckCommand();
//                serviceCheckCommand.setStatusRunner(command.getStatusRunner());
//                serviceCheckCommand.setServiceRoleName(command.getServiceRoleName());
//                serviceCheckActor.tell(serviceCheckCommand, ActorRef.noSender());
//            }
//        } else if (message instanceof ServiceRoleOperateCommand) {
//            ServiceRoleOperateCommand command = (ServiceRoleOperateCommand) message;
//            ServiceHandler serviceHandler = new ServiceHandler();
//            ExecResult execResult = null;
//            switch (command.getOperateType()) {
//                case START:
//                    execResult = serviceHandler.start(command.getStartRunner(), command.getDecompressPackageName());
//                case STOP:
//                    execResult = serviceHandler.stop(command.getStopRunner());
//                case RESTART:
//                    execResult = serviceHandler.reStart(command.getRestartRunner());
//                default:
//                    break;
//            }
//            ServiceRoleOperateCommandResult commandResult = new ServiceRoleOperateCommandResult();
//            BeanUtil.copyProperties(execResult, commandResult);
//            commandResult.setServiceRoleOperateCommand(command);
//            masterActor.tell(commandResult, ActorRef.noSender());
//        } else {
//            unhandled(message);
//        }
//    }
//}
