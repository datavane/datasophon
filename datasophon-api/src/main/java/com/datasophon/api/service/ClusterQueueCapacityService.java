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
import com.datasophon.dao.entity.ClusterQueueCapacity;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 *
 * @author dygao2
 * @email dygao2@datasophon.com
 * @date 2022-11-25 14:30:11
 */
public interface ClusterQueueCapacityService extends IService<ClusterQueueCapacity> {

    Result refreshToYarn(Integer clusterId) throws Exception;

    void createDefaultQueue(Integer id);

    Result listCapacityQueue(Integer clusterId);
}
