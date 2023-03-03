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

package com.datasophon.api.strategy;


import com.datasophon.api.load.ServiceConfigMap;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NameNodeHandlerStrategy extends ServiceHandlerAbstract  implements ServiceRoleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(NameNodeHandlerStrategy.class);

    private static final String ENABLE_RACK = "enableRack";

    private static final String ENABLE_KERBEROS = "enableKerberos";

    @Override
    public void handler(Integer clusterId, List<String> hosts) {

        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);

        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${nn1}", hosts.get(0));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${nn2}", hosts.get(1));

    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);

        boolean enableRack = false;
        boolean enableKerberos = false;
        Map<String, ServiceConfig> map = ProcessUtils.translateToMap(list);

        String key = clusterInfo.getClusterFrame() + Constants.UNDERLINE + "HDFS" + Constants.CONFIG;
        List<ServiceConfig> configs = ServiceConfigMap.get(key);

        for (ServiceConfig config : list) {
            if (ENABLE_RACK.equals(config.getName())) {
                if( (Boolean)config.getValue()){
                    enableRack = isEnableRack(enableRack,config);
                }
            }
            if(ENABLE_KERBEROS.equals(config.getName())){
                enableKerberos = isEnableKerberos(clusterId,globalVariables,enableKerberos,config,"HDFS");
            }
        }
        List<ServiceConfig> rackConfigs = new ArrayList<>();
        if(enableRack){
            logger.info("start to add rack config");
            addConfigWithRack(globalVariables, map, configs, rackConfigs);
        }else{
            removeConfigWithRack(list, map, configs);
        }
        list.addAll(rackConfigs);

        ArrayList<ServiceConfig> kbConfigs = new ArrayList<>();
        if(enableKerberos){
            addConfigWithKerberos(globalVariables, map, configs, kbConfigs);
        }else{
            removeConfigWithKerberos(list, map, configs);
        }
        list.addAll(kbConfigs);
    }



    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }

    @Override
    public void handlerServiceRoleInfo(ServiceRoleInfo serviceRoleInfo, String hostname) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + serviceRoleInfo.getClusterId());
        if( hostname.equals(globalVariables.get("${nn2}"))){
            logger.info("set to slave namenode");
            serviceRoleInfo.setSlave(true);
            serviceRoleInfo.setSortNum(5);
        }
    }

    @Override
    public void handlerServiceRoleCheck(ClusterServiceRoleInstanceEntity roleInstanceEntity, Map<String, ClusterServiceRoleInstanceEntity> map) {

    }


}
