/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.datasophon.worker.strategy;

import akka.actor.ActorRef;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.OlapOpsType;
import com.datasophon.common.command.OlapSqlExecCommand;
import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.model.ServiceRoleRunner;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ThrowableUtils;
import com.datasophon.worker.handler.ServiceHandler;
import com.datasophon.worker.utils.ActorUtils;

import java.util.ArrayList;

public class FEHandlerStrategy extends AbstractHandlerStrategy implements ServiceRoleStrategy {

    public FEHandlerStrategy(String serviceName,String serviceRoleName) {
        super(serviceName,serviceRoleName);
    }

    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler(command.getServiceName(), command.getServiceRoleName());
        if (command.getCommandType() == CommandType.INSTALL_SERVICE) {
            if (command.isSlave()) {
                logger.info("first start  fe");
                ArrayList<String> commands = new ArrayList<>();
                commands.add("--helper");
                commands.add(command.getMasterHost() + ":9010");
                commands.add("--daemon");

                ServiceRoleRunner startRunner = new ServiceRoleRunner();
                startRunner.setProgram(command.getStartRunner().getProgram());
                startRunner.setArgs(commands);
                startRunner.setTimeout("60");
                startResult = serviceHandler.start(startRunner, command.getStatusRunner(),
                        command.getDecompressPackageName(), command.getRunAs());
                if (startResult.getExecResult()) {
                    // add follower
                    try {
                        OlapSqlExecCommand sqlExecCommand = new OlapSqlExecCommand();
                        sqlExecCommand.setFeMaster(command.getMasterHost());
                        sqlExecCommand.setHostName(CacheUtils.getString(Constants.HOSTNAME));
                        sqlExecCommand.setOpsType(OlapOpsType.ADD_FE);
                        ActorUtils.getRemoteActor(command.getManagerHost(), "masterNodeProcessingActor")
                                .tell(sqlExecCommand, ActorRef.noSender());
                    } catch (Exception e) {
                        logger.info("add slave fe failed {}", ThrowableUtils.getStackTrace(e));
                    }
                    logger.info("slave fe start success");
                } else {
                    logger.info("slave fe start failed");
                }
            } else {
                startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(),
                        command.getDecompressPackageName(), command.getRunAs());
            }
        } else {
            startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(),
                    command.getDecompressPackageName(), command.getRunAs());
        }
        return startResult;
    }
}
