package com.datasophon.worker.task;

import akka.actor.ActorSelection;
import cn.hutool.cron.task.Task;
import com.datasophon.common.Constants;
import com.datasophon.common.command.ServiceCheckCommand;
import com.datasophon.common.model.ServiceRoleRunner;
import com.datasophon.common.utils.PropertyUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceCheckTask implements Task {
    private ServiceCheckCommand command;

    private ActorSelection masterActor;

    public ServiceCheckTask(ServiceCheckCommand command, ActorSelection masterActor) {
        this.masterActor = masterActor;
        this.command = command;
    }

    @Override
    public void execute() {
        ServiceRoleRunner statusRunner = command.getStatusRunner();
        String shell = statusRunner.getProgram();
        List<String> args = statusRunner.getArgs();
        long timeout = Long.parseLong(statusRunner.getTimeout());
        String params = args.stream().collect(Collectors.joining(" "));
        String frameCode = PropertyUtils.getString(Constants.FRAME_CODE);
        String frameVersion = PropertyUtils.getString(Constants.FRAME_VERSION);

        String workPath = Constants.DATA_SOPHON+Constants.SINGLE_SLASH+frameCode+Constants.SINGLE_SLASH+frameVersion+Constants.SINGLE_SLASH+command.getServiceName()+Constants.SINGLE_SLASH+Constants.SCRIPT;

//        ExecResult checkResult = ShellUtils.execWithStatus(workPath, workPath+Constants.SINGLE_SLASH +shell + " " + params, timeout);
        //通知管理端服务角色异常
//        if (!checkResult.getExecResult()){
//            AkkaRemoteReply akkaRemoteReply = new AkkaRemoteReply();
//            akkaRemoteReply.setReplyType(ReplyType.SERVICE_ROLE_STOP);
//            akkaRemoteReply.setMsg(command.getServiceRoleName()+" stop");
//            akkaRemoteReply.setServiceRoleName(command.getServiceRoleName());
//            akkaRemoteReply.setHostCommandId(command.getHostCommandId());
//
//            masterActor.tell(akkaRemoteReply, ActorRef.noSender());
//        }
    }
}
