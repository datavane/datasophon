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
import com.datasophon.dao.entity.ClusterServiceInstanceEntity;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 集群服务表
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-04-24 16:25:17
 */
public interface ClusterServiceInstanceService extends IService<ClusterServiceInstanceEntity> {

    ClusterServiceInstanceEntity getServiceInstanceByClusterIdAndServiceName(Integer clusterId, String parentName);

    String getServiceConfigByClusterIdAndServiceName(Integer id, String node);

    Result listAll(Integer clusterId);

    Result downloadClientConfig(Integer clusterId, String serviceName);

    Result getServiceRoleType(Integer serviceInstanceId);

    Result configVersionCompare(Integer serviceInstanceId, Integer roleGroupId);

    Result delServiceInstance(Integer serviceInstanceId);

    List<ClusterServiceInstanceEntity> listRunningServiceInstance(Integer clusterId);
}
