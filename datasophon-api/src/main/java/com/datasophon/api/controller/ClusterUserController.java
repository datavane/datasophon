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

import java.util.Arrays;
import java.util.Map;

import com.datasophon.api.service.ClusterUserService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "集群用户")
@RestController
@RequestMapping("cluster/user")
public class ClusterUserController {
    @Autowired
    private ClusterUserService clusterUserService;

    /**
     * 列表
     */
    @ApiOperation(value = "用户列表")
    @PostMapping("/list")
    public Result list(Integer clusterId, String username, Integer page, Integer pageSize){


        return clusterUserService.listPage(clusterId ,username,page,pageSize);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "用户信息")
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterUser clusterUser = clusterUserService.getById(id);

        return Result.success().put("clusterUser", clusterUser);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "创建用户")
    @PostMapping("/create")
    public Result save(Integer clusterId ,String username,Integer mainGroupId,String otherGroupIds){

        return clusterUserService.create(clusterId,username,mainGroupId,otherGroupIds);
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改用户信息")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterUser clusterUser){

        clusterUserService.updateById(clusterUser);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    public Result delete(Integer id){
        return clusterUserService.deleteClusterUser(id);
    }

}
