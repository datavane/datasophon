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

import com.datasophon.api.service.ClusterYarnSchedulerService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterYarnScheduler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 
 *
 * @author dygao2
 * @email dygao2@datasophon.com
 * @date 2022-11-25 15:02:11
 */
@Api(tags = "集群Yarn调度")
@RestController
@RequestMapping("cluster/yarn/scheduler")
public class ClusterYarnSchedulerController {
    @Autowired
    private ClusterYarnSchedulerService clusterYarnSchedulerService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public Result list(){


        return Result.success();
    }


    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @PostMapping("/info")
    public Result info(Integer clusterId){
        ClusterYarnScheduler clusterYarnScheduler = clusterYarnSchedulerService.getScheduler(clusterId);

        return Result.success(clusterYarnScheduler.getScheduler());
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterYarnScheduler clusterYarnScheduler){
        clusterYarnSchedulerService.save(clusterYarnScheduler);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterYarnScheduler clusterYarnScheduler){

        clusterYarnSchedulerService.updateById(clusterYarnScheduler);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        clusterYarnSchedulerService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
