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

package com.datasophon.worker.actor;

import com.datasophon.common.command.remote.CreateUnixGroupCommand;
import com.datasophon.common.command.remote.DelUnixGroupCommand;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.worker.utils.UnixUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

public class UnixGroupActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(UnixGroupActor.class);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof CreateUnixGroupCommand) {
            CreateUnixGroupCommand command = (CreateUnixGroupCommand) msg;
            ExecResult execResult = UnixUtils.createUnixGroup(command.getGroupName());
            logger.info("create unix group {} {}", command.getGroupName(),
                    execResult.getExecResult() ? "success" : "failed");
            getSender().tell(execResult, getSelf());
        } else if (msg instanceof DelUnixGroupCommand) {
            DelUnixGroupCommand command = (DelUnixGroupCommand) msg;
            ExecResult execResult = UnixUtils.delUnixGroup(command.getGroupName());
            logger.info("del unix group {} {}", command.getGroupName(),
                    execResult.getExecResult() ? "success" : "failed");
            getSender().tell(execResult, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
