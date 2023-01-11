package com.datasophon.api.master;


import akka.actor.UntypedActor;
import cn.hutool.core.util.ObjectUtil;
import com.datasophon.api.master.handler.host.*;
import com.datasophon.api.utils.MinaUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.command.HostAgentCommand;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ShellUtils;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.util.ArrayList;


public class HostAgentCommandActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(HostAgentCommandActor.class);

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception, Exception {
        logger.info("hostAgent command actor {}", reason.getMessage());
        super.preRestart(reason, message);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        HostAgentCommand hostAgentCommand = (HostAgentCommand) message;
        HostInfo hostInfo = hostAgentCommand.getHostInfo();
        logger.info("hostAgent command:{}", hostAgentCommand.getCommandType());
        ClientSession session = MinaUtils.openConnection(
                hostInfo.getHostname(),
                hostInfo.getSshPort(),
                hostInfo.getSshUser(),
                Constants.SLASH + hostInfo.getSshUser() + Constants.ID_RSA);
        String  commandType=hostAgentCommand.getCommandType();
        //执行worker启停操作
        MinaUtils.execCmdWithResult( session,"service datasophon-worker "+commandType);
        logger.info("hostAgent command:{}", "service datasophon-worker "+commandType);
        //同时执行node_exporter启停等操作
        String cpuArchitecture = ShellUtils.getCpuArchitecture();
        String workDir = Constants.WORKER_PATH;
        ArrayList<String> commands = new ArrayList<>();
        commands.add("sh");
        if (Constants.x86_64.equals(cpuArchitecture)) {
            commands.add(workDir + "/node/x86/control.sh");
        } else {
            commands.add(workDir + "/node/arm/control.sh");
        }
        commands.add(commandType);
        commands.add("node");
        ShellUtils.execWithStatus(Constants.WORKER_PATH, commands, 60L);

        if (ObjectUtil.isNotEmpty(session)) {
            session.close();
        }
    }
}
