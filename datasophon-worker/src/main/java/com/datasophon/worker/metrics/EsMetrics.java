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

package com.datasophon.worker.metrics;

import cn.hutool.core.io.FileUtil;
import com.datasophon.common.Constants;
import com.datasophon.common.model.ServiceRoleRunner;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.worker.handler.ServiceHandler;

import java.util.ArrayList;

/**
 *
 */
public class EsMetrics implements EsMetricsMXBean {

    private Integer esUp;


    @Override
    public Integer getEsUp() {
        if(FileUtil.exist(Constants.INSTALL_PATH+"/elasticsearch-7.16.2")){
            ServiceHandler serviceHandler = new ServiceHandler();
            ServiceRoleRunner serviceRoleRunner = new ServiceRoleRunner();
            serviceRoleRunner.setTimeout("60");
            ArrayList<String> args = new ArrayList<>();
            args.add("status");
            args.add("elasticsearch");
            serviceRoleRunner.setArgs(args);
            serviceRoleRunner.setProgram("control_es.sh");
            ExecResult status = serviceHandler.status(serviceRoleRunner, "elasticsearch-7.16.2");
            if(status.getExecResult()){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Integer setEsUp(Integer esUp) {
        return this.esUp = esUp;
    }
}