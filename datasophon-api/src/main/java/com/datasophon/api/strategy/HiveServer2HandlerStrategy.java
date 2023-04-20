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

import com.datasophon.api.load.GlobalVariables;
import com.datasophon.api.load.ServiceConfigMap;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.common.utils.PlaceholderUtils;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveServer2HandlerStrategy extends ServiceHandlerAbstract implements ServiceRoleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(HiveServer2HandlerStrategy.class);
    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        Map<String, String> globalVariables = GlobalVariables.get(clusterId);
        CacheUtils.put("enableHiveServer2HA", false);
        if (hosts.size() > 1) {
            CacheUtils.put("enableHiveServer2HA", true);
            ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${masterHiveServer2}", hosts.get(0));
        }
    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        Map<String, String> globalVariables = GlobalVariables.get(clusterId);
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        boolean enableKerberos = false;
        Map<String, ServiceConfig> map = ProcessUtils.translateToMap(list);
        for (ServiceConfig config : list) {
            if ("enableKerberos".equals(config.getName())) {
                enableKerberos = isEnableKerberos(clusterId, globalVariables, enableKerberos, config, "HIVE");
            }

        }
        String key = clusterInfo.getClusterFrame() + Constants.UNDERLINE + "HIVE" + Constants.CONFIG;
        List<ServiceConfig> configs = ServiceConfigMap.get(key);
        ArrayList<ServiceConfig> kbConfigs = new ArrayList<>();
        if (enableKerberos) {
            addConfigWithKerberos(globalVariables, map, configs, kbConfigs);
        } else {
            removeConfigWithKerberos(list, map, configs);
        }
        list.addAll(kbConfigs);

    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {
        // if enabled hiveserver2 ha
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        List<ServiceConfig> serviceConfigs =
                ServiceConfigMap.get(clusterInfo.getClusterFrame() + Constants.UNDERLINE + "HIVE" + Constants.CONFIG);
        Map<String, String> globalVariables = GlobalVariables.get(clusterId);
        if ((Boolean) CacheUtils.get("enableHiveServer2HA")) {
            for (ServiceConfig serviceConfig : serviceConfigs) {
                if ("ha".equals(serviceConfig.getConfigType())) {
                    serviceConfig.setRequired(true);
                    serviceConfig.setHidden(false);
                    if (Constants.INPUT.equals(serviceConfig.getType())) {
                        String value = PlaceholderUtils.replacePlaceholders((String) serviceConfig.getValue(),
                                globalVariables, Constants.REGEX_VARIABLE);
                        serviceConfig.setValue(value);
                    }
                    list.add(serviceConfig);
                }
            }
        } else {
            for (ServiceConfig serviceConfig : serviceConfigs) {
                if ("ha".equals(serviceConfig.getConfigType())) {
                    serviceConfig.setRequired(false);
                    serviceConfig.setHidden(true);
                }
            }
        }
    }

    @Override
    public void handlerServiceRoleInfo(ServiceRoleInfo serviceRoleInfo, String hostname) {
        Map<String, String> globalVariables = GlobalVariables.get(serviceRoleInfo.getClusterId());
        if (globalVariables.containsKey("${masterHiveServer2}")
                && !hostname.equals(globalVariables.get("${masterHiveServer2}"))) {
            logger.info("set to slave hiveserver2");
            serviceRoleInfo.setSlave(true);
        }
    }

    @Override
    public void handlerServiceRoleCheck(ClusterServiceRoleInstanceEntity roleInstanceEntity,
                                        Map<String, ClusterServiceRoleInstanceEntity> map) {

    }
}
