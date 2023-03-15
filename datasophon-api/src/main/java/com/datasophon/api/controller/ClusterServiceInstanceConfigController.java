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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datasophon.dao.entity.ClusterServiceInstanceConfigEntity;
import com.datasophon.api.service.ClusterServiceInstanceConfigService;
import com.datasophon.common.utils.Result;



/**
 * 集群服务角色实例配置表
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-04-24 16:25:17
 */
@Api(tags = "集群服务实例配置")
@RestController
@RequestMapping("cluster/service/instance/config")
public class ClusterServiceInstanceConfigController {
    @Autowired
    private ClusterServiceInstanceConfigService clusterServiceInstanceConfigService;

    /**
     * 列表
     */
    @ApiOperation(value = "获取配置版本")
    @PostMapping("/getConfigVersion")
    public Result getConfigVersion(Integer serviceInstanceId,Integer roleGroupId){
        return clusterServiceInstanceConfigService.getConfigVersion(serviceInstanceId,roleGroupId);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "服务实例配置信息")
    @PostMapping("/info")
    public Result info(Integer serviceInstanceId,Integer version,Integer roleGroupId,Integer page,Integer pageSize){
        return clusterServiceInstanceConfigService.getServiceInstanceConfig(serviceInstanceId,version,roleGroupId,page,pageSize);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存服务实例配置")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterServiceInstanceConfigEntity clusterServiceInstanceConfig){
        clusterServiceInstanceConfigService.save(clusterServiceInstanceConfig);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改服务实例配置")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterServiceInstanceConfigEntity clusterServiceInstanceConfig){
        clusterServiceInstanceConfigService.updateById(clusterServiceInstanceConfig);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除服务实例配置")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        clusterServiceInstanceConfigService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
