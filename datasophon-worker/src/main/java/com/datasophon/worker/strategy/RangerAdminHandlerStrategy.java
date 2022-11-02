package com.datasophon.worker.strategy;

import com.datasophon.common.Constants;
import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.worker.handler.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class RangerAdminHandlerStrategy implements ServiceRoleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RangerAdminHandlerStrategy.class);

    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler();
        //执行setup.sh setup_global.sh
        //执行hdfs namenode -bootstrapStandby
        logger.info("start to execute ranger admin setup.sh");
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName() + Constants.SLASH + "setup.sh");
        ExecResult execResult = ShellUtils.execWithStatus(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName(), commands, 300L);

        ArrayList<String> globalCommand = new ArrayList<>();
        globalCommand.add(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName() + Constants.SLASH + "set_globals.sh");
        ExecResult globalResult = ShellUtils.execWithStatus(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName(), globalCommand, 300L);
        if (execResult.getExecResult() && globalResult.getExecResult()) {
            logger.info("ranger admin setup success");
            startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(),command.getRunAs());
        } else {
            logger.info("ranger admin setup failed");
        }
        return startResult;
    }
}
