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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterZkService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterZk;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ZkServerHandlerStrategy implements ServiceRoleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ZkServerHandlerStrategy.class);

    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        //保存zkUrls到全局变量
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        String join = String.join(":2181,", hosts);
        String zkUrls = join + ":2181";
        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${zkUrls}", zkUrls);

    }



    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {

    }
    /**
     * 查询现有的zkserver
     * 比较获取新增的zkserver
     * 给新增的zkserver赋值myid
     *
     * @param clusterId
     * @param list
     */
    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {
        //添加server.x配置
        ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);
        ClusterInfoEntity clusterInfo = clusterInfoService.getById(clusterId);
        ClusterZkService zkService = SpringTool.getApplicationContext().getBean(ClusterZkService.class);
        String hostMapKey = clusterInfo.getClusterCode() + Constants.UNDERLINE + Constants.SERVICE_ROLE_HOST_MAPPING;
        HashMap<String, List<String>> hostMap = (HashMap<String, List<String>>) CacheUtils.get(hostMapKey);
        List<ClusterZk> zkList = zkService.list(new QueryWrapper<ClusterZk>().eq(Constants.CLUSTER_ID, clusterId));
        List<String> zkServerList = zkList.stream().map(e -> e.getZkServer()).collect(Collectors.toList());
        Integer maxMyId = zkService.getMaxMyId(clusterId);
        logger.info("zk max myid is {}", maxMyId);
        if (Objects.nonNull(hostMap)) {
            List<String> zkServers = hostMap.get("ZkServer");
            HashMap<String, String> hostIpMap = (HashMap<String, String>) CacheUtils.get(Constants.HOST_IP);

            HashMap<String, ServiceConfig> map = new HashMap<>();
            for (ServiceConfig serviceConfig : list) {
                map.put(serviceConfig.getName(), serviceConfig);
            }
            List<String> subtractList = new ArrayList<>(CollectionUtils.subtract(zkServers, zkServerList));
            logger.info("zk subtractList is : {}", subtractList.toString());
            int i = 0;
            if (Objects.nonNull(maxMyId)) {
                i = maxMyId;
            }
            for (String server : subtractList) {
                Integer myid = i + 1;
                ServiceConfig serviceConfig = new ServiceConfig();
                serviceConfig.setName("server." + myid);
                serviceConfig.setLabel("server." + myid);
                serviceConfig.setValue(hostIpMap.get(server) + ":2888:3888");
                serviceConfig.setHidden(false);
                serviceConfig.setRequired(true);
                serviceConfig.setType("input");
                serviceConfig.setDefaultValue("");
                serviceConfig.setConfigType("zkserver");
                if (map.containsKey("server." + myid)) {
                    logger.info("set zk server {}", myid);
                    ServiceConfig config = map.get("server." + myid);
                    BeanUtils.copyProperties(serviceConfig,config);
                } else {
                    logger.info("add zk server.x config");
                    list.add(serviceConfig);
                }
                CacheUtils.put("zkserver_" + server, myid);
                i++;
            }
        }
    }
}
