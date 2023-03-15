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

import com.datasophon.api.service.ClusterServiceInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.datasophon.dao.entity.ClusterServiceInstanceEntity;
import com.datasophon.common.utils.Result;



/**
 * 集群服务表
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-04-24 16:25:17
 */
@Api(tags = "集群服务")
@RestController
@RequestMapping("cluster/service/instance")
public class ClusterServiceInstanceController {
    @Autowired
    private ClusterServiceInstanceService clusterServiceInstanceService;

    /**
     * 列表
     */
    @ApiOperation(value = "服务实例列表")
    @PostMapping("/list")
    public Result list(Integer clusterId){
        return clusterServiceInstanceService.listAll(clusterId);
    }

    /**
     * 获取服务角色类型列表
     */
    @ApiOperation(value = "获取服务角色类型列表")
    @PostMapping("/getServiceRoleType")
    public Result getServiceRoleType(Integer serviceInstanceId){
        return clusterServiceInstanceService.getServiceRoleType(serviceInstanceId);
    }


    /**
     * 获取服务角色类型列表
     */
    @ApiOperation(value = "配置版本比较")
    @PostMapping("/configVersionCompare")
    public Result configVersionCompare(Integer serviceInstanceId,Integer roleGroupId){
        return clusterServiceInstanceService.configVersionCompare(serviceInstanceId,roleGroupId);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "服务实例信息")
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterServiceInstanceEntity clusterServiceInstance = clusterServiceInstanceService.getById(id);

        return Result.success().put("clusterServiceInstance", clusterServiceInstance);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "下载客户端配置")
    @PostMapping("/downloadClientConfig")
    public Result downloadClientConfig(Integer clusterId,String serviceName){

        return clusterServiceInstanceService.downloadClientConfig(clusterId,serviceName);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存服务实例")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterServiceInstanceEntity clusterServiceInstance){
        clusterServiceInstanceService.save(clusterServiceInstance);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改服务实例")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterServiceInstanceEntity clusterServiceInstance){
        clusterServiceInstanceService.updateById(clusterServiceInstance);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除服务实例")
    @PostMapping("/delete")
    public Result delete(Integer serviceInstanceId){
        return clusterServiceInstanceService.delServiceInstance(serviceInstanceId);
    }

}
