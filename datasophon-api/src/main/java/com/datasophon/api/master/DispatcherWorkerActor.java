package com.datasophon.api.master;

import akka.actor.UntypedActor;
import com.datasophon.api.master.handler.host.*;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.command.DispatcherHostAgentCommand;
import com.datasophon.common.model.HostInfo;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;


public class DispatcherWorkerActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherWorkerActor.class);

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        logger.info("host actor restart because {}", reason.getMessage());
        super.preRestart(reason, message);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        DispatcherHostAgentCommand command = (DispatcherHostAgentCommand) message;
        HostInfo hostInfo = command.getHostInfo();
        logger.info("start dispatcher host agent :{}", hostInfo.getHostname());
        hostInfo.setMessage("开始分发主机管理agent安装包");
        Session session = JSchUtils.getSession(
                hostInfo.getHostname(),
                hostInfo.getSshPort(),
                hostInfo.getSshUser(),
                Constants.SLASH + hostInfo.getSshUser() + Constants.ID_RSA);
        DispatcherWorkerHandlerChain handlerChain = new DispatcherWorkerHandlerChain();
        handlerChain.addHandler(new UploadWorkerHandler());
        handlerChain.addHandler(new CheckWorkerMd5Handler());
        handlerChain.addHandler(new DecompressWorkerHandler());
        handlerChain.addHandler(new InstallJDKHandler());
        handlerChain.addHandler(new StartWorkerHandler(command.getClusterId(),command.getClusterFrame()));
        handlerChain.handle(session,hostInfo);
    }
}
