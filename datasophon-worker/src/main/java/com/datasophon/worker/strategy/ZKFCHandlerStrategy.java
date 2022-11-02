package com.datasophon.worker.strategy;

import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.worker.handler.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ZKFCHandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(ZKFCHandlerStrategy.class);
    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler();
        if(!command.isSlave()  && command.getCommandType().equals(CommandType.INSTALL_SERVICE)){
            logger.info("start to execute hdfs zkfc -formatZK");

            ArrayList<String> commands = new ArrayList<>();
            commands.add("/opt/datasophon/hadoop-3.3.3/bin/hdfs");
            commands.add("zkfc");
            commands.add("-formatZK");
            ExecResult execResult = ShellUtils.execWithStatus("/opt/datasophon/hadoop-3.3.3", commands, 30L);
            if (execResult.getExecResult()){
                logger.info("zkfc format success");
                startResult = serviceHandler.start(command.getStartRunner(),command.getStatusRunner(),command.getDecompressPackageName(),command.getRunAs());
            }else{
                logger.info("zkfc format failed");
            }
        }else{
            startResult = serviceHandler.start(command.getStartRunner(),command.getStatusRunner(),command.getDecompressPackageName(),command.getRunAs());
        }
        return startResult;
    }
}
