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
 *
 */

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
