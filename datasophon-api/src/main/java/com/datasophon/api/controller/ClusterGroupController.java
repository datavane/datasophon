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

import com.datasophon.api.service.ClusterGroupService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "集群组")
@RestController
@RequestMapping("cluster/group")
public class ClusterGroupController {
    @Autowired
    private ClusterGroupService clusterGroupService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public Result list(String groupName, Integer page, Integer pageSize){


        return clusterGroupService.listPage(groupName,page,pageSize);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterGroup clusterGroup = clusterGroupService.getById(id);

        return Result.success().put("clusterGroup", clusterGroup);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public Result save(Integer clusterId, String groupName){
        return  clusterGroupService.saveClusterGroup(clusterId,groupName);
    }
    /**
     * 删除用户组
     */
    @ApiOperation(value = "删除用户组")
    @PostMapping("/delete")
    public Result delete(Integer id){
        return clusterGroupService.deleteUserGroup(id);
    }


    /**
     * 刷新用户组到主机
     */
    @ApiOperation(value = "刷新用户组到主机")
    @PostMapping("/refreshUserGroupToHost")
    public Result refreshUserGroupToHost(Integer clusterId){

        clusterGroupService.refreshUserGroupToHost(clusterId);
        
        return Result.success();
    }


}
