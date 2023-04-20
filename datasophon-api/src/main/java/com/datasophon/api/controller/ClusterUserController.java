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

package com.datasophon.api.controller;

import com.datasophon.api.service.ClusterUserService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cluster/user")
public class ClusterUserController {

    @Autowired
    private ClusterUserService clusterUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(Integer clusterId, String username, Integer page, Integer pageSize) {

        return clusterUserService.listPage(clusterId, username, page, pageSize);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        ClusterUser clusterUser = clusterUserService.getById(id);

        return Result.success().put("clusterUser", clusterUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    public Result save(Integer clusterId, String username, Integer mainGroupId, String otherGroupIds) {

        return clusterUserService.create(clusterId, username, mainGroupId, otherGroupIds);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody ClusterUser clusterUser) {

        clusterUserService.updateById(clusterUser);

        return Result.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        return clusterUserService.deleteClusterUser(id);
    }

}
