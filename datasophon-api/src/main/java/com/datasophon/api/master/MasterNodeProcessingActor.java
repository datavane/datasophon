package com.datasophon.api.master;

import com.datasophon.common.Constants;
import com.datasophon.common.command.StarrocksSqlExecCommand;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.StarRocksUtils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

public class MasterNodeProcessingActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(MasterNodeProcessingActor.class);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StarrocksSqlExecCommand) {
            StarrocksSqlExecCommand command = (StarrocksSqlExecCommand) message;
            if (Constants.ADD_BE.equals(command.getOpsType())) {
                ExecResult execResult = Constants.ADD_BE.equals(command.getOpsType())
                        ? StarRocksUtils.addBackendSqlClient(command.getFeMaster(), command.getHostName())
                        : StarRocksUtils.addFollowerBySqlClient(command.getFeMaster(), command.getHostName());
                String tip = Constants.ADD_BE.equals(command.getOpsType()) ? "backend" : "follower";
                if (execResult.getExecResult()) {
                    logger.info(command.getHostName() + " " + tip + " be added success");
                } else {
                    logger.info(command.getHostName() + " " + tip + " be added failed");
                }
                int tryTimes = 0;
                while (!execResult.getExecResult() && tryTimes < 3) {
                    try {
                        TimeUnit.SECONDS.sleep(10L);
                        execResult = Constants.ADD_BE.equals(command.getOpsType())
                                ? StarRocksUtils.addBackendSqlClient(command.getFeMaster(), command.getHostName())
                                : StarRocksUtils.addFollowerBySqlClient(command.getFeMaster(), command.getHostName());
                        if (execResult.getExecResult()) {
                            logger.info(command.getHostName() + " " + tip + " be added success");
                            break;
                        } else {
                            logger.info(command.getHostName() + " " + tip + " be added failed");
                        }
                        tryTimes++;
                    } catch (InterruptedException e) {
                        logger.info("The SR operate be sleep operation failed");
                    }
                }
            }
        } else {
            unhandled(message);
        }
    }
}
