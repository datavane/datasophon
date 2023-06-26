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

package com.datasophon.api.service;

import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterHostEntity;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 集群主机表 
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-04-14 20:32:39
 */
public interface ClusterHostService extends IService<ClusterHostEntity> {

    ClusterHostEntity getClusterHostByHostname(String hostname);

    Result listByPage(Integer clusterId, String hostname, String ip, String cpuArchitecture, Integer hostState,
                      String orderField, String orderType, Integer page, Integer pageSize);

    List<ClusterHostEntity> getHostListByClusterId(Integer id);

    Result getRoleListByHostname(Integer clusterId, String hostname);

    Result deleteHost(Integer hostId);

    /**
     * 批量删除主机。
     * 删除主机，首先停止主机上的服务
     * 其次删除主机 worker，同时移除 Prometheus hosts
     * 然后删除主机运行的实例
     * @param hostIds
     * @return
     */
    Result deleteHosts(String hostIds);

    Result getRack(Integer clusterId);

    void deleteHostByClusterId(Integer id);

    void updateBatchNodeLabel(List<String> hostIds, String nodeLabel);

    List<ClusterHostEntity> getHostListByIds(List<String> ids);

    Result assignRack(Integer clusterId, String rack, String hostIds);

    List<ClusterHostEntity> getClusterHostByRack(Integer clusterId, String rack);
}
