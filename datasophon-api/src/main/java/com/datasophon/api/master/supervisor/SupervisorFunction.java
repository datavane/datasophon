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

package com.datasophon.api.master.supervisor;


import akka.actor.SupervisorStrategy;
import akka.japi.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SupervisorFunction implements Function<Throwable, SupervisorStrategy.Directive> {

    private static final Logger logger = LoggerFactory.getLogger(SupervisorFunction.class);

    @Override
    public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
        if(param instanceof ArithmeticException){
            logger.info("meet ArithmeticException,just resume");
            return SupervisorStrategy.resume();
        }else if(param instanceof  NullPointerException){
            logger.info("meet NullPointerException,restart");
            return SupervisorStrategy.restart();
        }else if(param instanceof IllegalArgumentException){
            return SupervisorStrategy.stop();
        } else if(param instanceof SQLException){
            logger.info("meet SQLException,restart");
            return SupervisorStrategy.restart();
        }else {
            return SupervisorStrategy.restart();
        }
    }
}
