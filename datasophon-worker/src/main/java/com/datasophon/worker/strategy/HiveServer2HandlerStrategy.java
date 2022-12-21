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

import cn.hutool.core.io.FileUtil;
import com.datasophon.common.Constants;
import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.worker.handler.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class HiveServer2HandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(HiveServer2HandlerStrategy.class);

    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler();
        if (command.getEnableRangerPlugin()) {
            logger.info("start to enable hive hdfs plugin");
            ArrayList<String> commands = new ArrayList<>();
            commands.add("sh");
            commands.add("./enable-hive-plugin.sh");
            if (!FileUtil.exist(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName() + "/ranger-hive-plugin/success.id")) {
                ExecResult execResult = ShellUtils.execWithStatus(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName() + "/ranger-hive-plugin", commands, 30L);
                if (execResult.getExecResult()) {
                    logger.info("enable ranger hive plugin success");
                    FileUtil.writeUtf8String("success", Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName() + "/ranger-hive-plugin/success.id");
                    startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(),command.getRunAs());
                } else {
                    logger.info("enable ranger hive plugin failed");
                }
            } else {
                startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(),command.getRunAs());
            }
        } else if (command.getCommandType() == CommandType.INSTALL_SERVICE && !command.isSlave()) {
            //初始化数据库
            ArrayList<String> commands = new ArrayList<>();
            commands.add("bin/schematool");
            commands.add("-dbType");
            commands.add("mysql");
            commands.add("-initSchema");
            ExecResult execResult = ShellUtils.execWithStatus(Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName(), commands, 60L);
            if (execResult.getExecResult()) {
                startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(),command.getRunAs());
            } else {
                logger.info("init hive schema failed");
            }
        } else {
            startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(),command.getRunAs());
        }
        return startResult;
    }
}
