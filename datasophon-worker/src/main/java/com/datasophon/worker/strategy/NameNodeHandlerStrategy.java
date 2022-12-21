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

public class NameNodeHandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(NameNodeHandlerStrategy.class);

    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) {
        logger.info("namenode start before");
        ExecResult startResult = new ExecResult();
        ServiceHandler serviceHandler = new ServiceHandler();
        String workPath = Constants.INSTALL_PATH + Constants.SLASH + command.getDecompressPackageName();
        if (command.getCommandType().equals(CommandType.INSTALL_SERVICE)) {
            if (command.isSlave()) {
                //执行hdfs namenode -bootstrapStandby
                logger.info("start to execute hdfs namenode -bootstrapStandby");
                ArrayList<String> commands = new ArrayList<>();
                commands.add(workPath + "/bin/hdfs");
                commands.add("namenode");
                commands.add("-bootstrapStandby");
                ExecResult execResult = ShellUtils.execWithStatus(workPath, commands, 30L);
                if (execResult.getExecResult()) {
                    logger.info("namenode standby success");
                    startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(), command.getRunAs());
                } else {
                    logger.info("namenode standby failed");
                }
            } else {
                logger.info("start to execute format namenode");
                ArrayList<String> commands = new ArrayList<>();
                commands.add(workPath + "/bin/hdfs");
                commands.add("namenode");
                commands.add("-format");
                commands.add("smhadoop");
                //清空namenode元数据
                FileUtil.del("/data/dfs/nn/current");
                ExecResult execResult = ShellUtils.execWithStatus(workPath, commands, 180L);
                if (execResult.getExecResult()) {
                    logger.info("namenode format success");
                    startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(), command.getRunAs());
                } else {
                    logger.info("namenode format failed");
                }
            }
        } else if (command.getEnableRangerPlugin()) {
            logger.info("start to enable ranger hdfs plugin");
            ArrayList<String> commands = new ArrayList<>();
            commands.add("sh");
            commands.add(workPath + "/ranger-hdfs-plugin/enable-hdfs-plugin.sh");
            if (!FileUtil.exist(workPath + "/ranger-hdfs-plugin/success.id")) {
                ExecResult execResult = ShellUtils.execWithStatus(workPath + "/ranger-hdfs-plugin", commands, 30L);
                if (execResult.getExecResult()) {
                    logger.info("enable ranger hdfs plugin success");
                    //写入ranger plugin集成成功标识
                    FileUtil.writeUtf8String("success", workPath + "/ranger-hdfs-plugin/success.id");
                    startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(), command.getRunAs());
                } else {
                    logger.info("enable ranger hdfs plugin failed");
                }
            } else {
                startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(), command.getRunAs());
            }
        } else {
            startResult = serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(), command.getRunAs());
        }
        return startResult;
    }
}
