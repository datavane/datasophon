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

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.enums.Status;
import com.datasophon.api.service.ClusterYarnQueueService;
import com.datasophon.common.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterYarnQueue;

/**
 * 
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-07-13 19:34:14
 */
@Api(tags = "集群Yarn队列")
@RestController
@RequestMapping("cluster/yarn/queue")
public class ClusterYarnQueueController {
    @Autowired
    private ClusterYarnQueueService clusterYarnQueueService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public Result list(Integer clusterId, Integer page,Integer pageSize){
        return clusterYarnQueueService.listByPage(clusterId,page,pageSize);
    }

    /**
     * 刷新队列
     */
    @ApiOperation(value = "刷新队列")
    @PostMapping("/refreshQueues")
    public Result refreshQueues(Integer clusterId) throws Exception {
        return clusterYarnQueueService.refreshQueues(clusterId);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterYarnQueue clusterYarnQueue = clusterYarnQueueService.getById(id);

        return Result.success().put("clusterYarnQueue", clusterYarnQueue);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterYarnQueue clusterYarnQueue){
        List<ClusterYarnQueue> list = clusterYarnQueueService.list(new QueryWrapper<ClusterYarnQueue>().eq(Constants.QUEUE_NAME, clusterYarnQueue.getQueueName()));
        if(Objects.nonNull(list) && list.size() == 1){
            return Result.error(Status.QUEUE_NAME_ALREADY_EXISTS.getMsg());
        }
        clusterYarnQueue.setCreateTime(new Date());
        clusterYarnQueueService.save(clusterYarnQueue);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterYarnQueue clusterYarnQueue){

        clusterYarnQueueService.updateById(clusterYarnQueue);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        clusterYarnQueueService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
