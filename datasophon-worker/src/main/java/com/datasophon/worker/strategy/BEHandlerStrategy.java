package com.datasophon.worker.strategy;

import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.worker.handler.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class BEHandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(BEHandlerStrategy.class);

    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler();
        startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(),command.getRunAs());
        if (command.getCommandType().equals(CommandType.INSTALL_SERVICE)) {
            logger.info("add starrocks be to cluster");
//            bin/start_fe.sh --helper hadoop101:9010 --daemon

            ArrayList<String> commands = new ArrayList<>();
            commands.add(command.getDecompressPackageName() + "fe/bin/start_fe.sh");
            commands.add("--helper");
            commands.add(command.getMasterHost() + ":9010");
            commands.add("--daemon");
            startResult = ShellUtils.execWithStatus(command.getDecompressPackageName(), commands, 30L);
            if (startResult.getExecResult()) {
                logger.info("slave fe start success");
            } else {
                logger.info("slave fe start failed");
            }
        }
        return startResult;
    }
}
