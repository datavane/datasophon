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
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.utils.Result;


/**
 * 集群服务角色实例表
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-04-24 16:25:17
 */
@Api(tags = "集群服务角色实例")
@RestController
@RequestMapping("cluster/service/role/instance")
public class ClusterServiceRoleInstanceController {
    @Autowired
    private ClusterServiceRoleInstanceService clusterServiceRoleInstanceService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public Result list(Integer serviceInstanceId,String hostname,Integer serviceRoleState, String serviceRoleName,Integer roleGroupId,Integer page, Integer pageSize) {
        return clusterServiceRoleInstanceService.listAll(serviceInstanceId,hostname,serviceRoleState,serviceRoleName,roleGroupId,page,pageSize);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取日志")
    @PostMapping("/getLog")
    public Result getLog(Integer serviceRoleInstanceId) throws Exception {
        return clusterServiceRoleInstanceService.getLog(serviceRoleInstanceId);
    }

    /**
     * 退役
     */
    @ApiOperation(value = "退役")
    @PostMapping("/decommissionNode")
    public Result decommissionNode(String serviceRoleInstanceIds,String serviceName) throws Exception {
        return clusterServiceRoleInstanceService.decommissionNode(serviceRoleInstanceIds,serviceName);
    }

    /**
     * 重启过时服务
     */
    @ApiOperation(value = "重启过时服务")
    @PostMapping("/restartObsoleteService")
    public Result restartObsoleteService(Integer roleGroupId) throws Exception {
        return clusterServiceRoleInstanceService.restartObsoleteService(roleGroupId);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterServiceRoleInstanceEntity clusterServiceRoleInstance) {
        clusterServiceRoleInstanceService.save(clusterServiceRoleInstance);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterServiceRoleInstanceEntity clusterServiceRoleInstance) {
        clusterServiceRoleInstanceService.updateById(clusterServiceRoleInstance);
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(String serviceRoleInstancesIds) {
        List<String> idList = Arrays.asList(serviceRoleInstancesIds.split(","));
        return clusterServiceRoleInstanceService.deleteServiceRole(idList);
    }

}
