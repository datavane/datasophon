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

import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.common.utils.StarRocksUtils;
import com.datasophon.common.utils.ThrowableUtils;
import com.datasophon.worker.handler.ServiceHandler;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BEHandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(BEHandlerStrategy.class);

    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler();

        if (command.getCommandType().equals(CommandType.INSTALL_SERVICE)) {
            logger.info("add  be to cluster");
            ShellUtils.exceShell("ulimit -n 102400");
            ShellUtils.exceShell("sysctl -w vm.max_map_count=2000000");
            startResult =
                    serviceHandler.start(
                            command.getStartRunner(),
                            command.getStatusRunner(),
                            command.getDecompressPackageName(),
                            command.getRunAs());
            if (startResult.getExecResult()) {
                try {
                    StarRocksUtils.addBackend(
                            command.getMasterHost(), CacheUtils.getString(Constants.HOSTNAME));
                } catch (SQLException | ClassNotFoundException e) {
                    logger.info("add backend failed {}", ThrowableUtils.getStackTrace(e));
                }
                logger.info("slave be start success");
            } else {
                logger.info("slave be start failed");
            }
        } else {
            startResult =
                    serviceHandler.start(
                            command.getStartRunner(),
                            command.getStatusRunner(),
                            command.getDecompressPackageName(),
                            command.getRunAs());
        }
        return startResult;
    }
}
