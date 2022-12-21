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

package com.datasophon.worker.actor;

import akka.actor.UntypedActor;
import com.datasophon.common.command.InstallServiceRoleCommand;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.worker.handler.InstallServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstallServiceActor extends UntypedActor {
    private static final Logger logger = LoggerFactory.getLogger(InstallServiceActor.class);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof InstallServiceRoleCommand){
            InstallServiceRoleCommand command = (InstallServiceRoleCommand) msg;
            InstallServiceHandler serviceHandler = new InstallServiceHandler();
            logger.info("start install package {}",command.getPackageName());
            ExecResult installResult = serviceHandler.install(command.getPackageName(), command.getDecompressPackageName(), command.getPackageMd5(),command.getRunAs());
            getSender().tell(installResult,getSelf());
            logger.info("install package {}",installResult.getExecResult()?"success":"failed");
        }else {
            unhandled(msg);
        }
    }
}
