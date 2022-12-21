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

package com.datasophon.api.service.strategy;

import com.alibaba.fastjson.JSONArray;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.FrameServiceService;
import com.datasophon.api.utils.PackageUtils;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.FrameServiceEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NameNodeHandlerStrategy implements ServiceRoleStrategy {


    @Override
    public void handler(Integer clusterId, List<String> hosts) {

        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);

        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${nn1}", hosts.get(0));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${nn2}", hosts.get(1));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${fs.defaultFS}", "nameservice1");


    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        for (ServiceConfig config : list) {
            if ("enableRack".equals(config.getName()) && (Boolean)config.getValue()) {
                ServiceConfig serviceConfig = ProcessUtils.createServiceConfig("net.topology.table.file.name",Constants.INSTALL_PATH +
                        Constants.SLASH +
                        PackageUtils.getServiceDcPackageName(clusterInfo.getClusterFrame(), "HDFS")+
                        "/etc/hadoop/rack.properties","input");
                ServiceConfig mapImplConfig = ProcessUtils.createServiceConfig("net.topology.node.switch.mapping.impl", "org.apache.hadoop.net.TableMapping","input");
                list.add(serviceConfig);
                list.add(mapImplConfig);
            }
        }

    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }


}
