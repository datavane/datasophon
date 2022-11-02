package com.datasophon.worker.actor;

import akka.actor.UntypedActor;
import com.datasophon.common.command.InstallServiceRoleCommand;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.worker.handler.InstallServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstallServiceActor extends UntypedActor {
    private static final Logger logger = LoggerFactory.getLogger(InstallServiceActor.class);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof InstallServiceRoleCommand){
            InstallServiceRoleCommand command = (InstallServiceRoleCommand) msg;
            InstallServiceHandler serviceHandler = new InstallServiceHandler();
            logger.info("start install package {}",command.getPackageName());
            ExecResult installResult = serviceHandler.install(command.getPackageName(), command.getDecompressPackageName(), command.getPackageMd5());
            getSender().tell(installResult,getSelf());
            logger.info("install package {}",installResult.getExecResult()?"success":"failed");
        }else {
            unhandled(msg);
        }
    }
}
