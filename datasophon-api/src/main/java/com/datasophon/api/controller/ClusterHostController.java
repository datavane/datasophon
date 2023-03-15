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

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.common.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.common.utils.Result;


/**
 * 集群主机表
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-04-14 20:32:39
 */
@Api(tags = "集群主机")
@RestController
@RequestMapping("api/cluster/host")
public class ClusterHostController {
    @Autowired
    private ClusterHostService clusterHostService;

    /**
     * 查询集群所有主机
     */
    @ApiOperation(value = "查询集群所有主机")
    @PostMapping("/all")
    public Result all(Integer clusterId) {
        List<ClusterHostEntity> list = clusterHostService.list(new QueryWrapper<ClusterHostEntity>().eq(Constants.CLUSTER_ID, clusterId)
                .eq(Constants.MANAGED, 1)
                .orderByAsc(Constants.HOSTNAME));
        return Result.success(list);
    }

    /**
     * 查询集群所有主机
     */
    @ApiOperation(value = "查询集群所有主机")
    @PostMapping("/list")
    public Result list(Integer clusterId, String hostname, String ip,String cpuArchitecture, Integer hostState, String orderField, String orderType, Integer page, Integer pageSize) {
        return clusterHostService.listByPage(clusterId, hostname, ip,cpuArchitecture, hostState, orderField, orderType, page, pageSize);

    }

    @ApiOperation(value = "按主机名获取角色列表")
    @PostMapping("/getRoleListByHostname")
    public Result getRoleListByHostname(Integer clusterId, String hostname) {
        return clusterHostService.getRoleListByHostname(clusterId, hostname);

    }

    @ApiOperation(value = "获取机架")
    @PostMapping("/getRack")
    public Result getRack(Integer clusterId) {
        return clusterHostService.getRack(clusterId);

    }

    @ApiOperation(value = "分配机架")
    @PostMapping("/assignRack")
    public Result assignRack(Integer clusterId, String rack, String hostIds) {
        return clusterHostService.assignRack(clusterId, rack, hostIds);

    }


    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        ClusterHostEntity clusterHost = clusterHostService.getById(id);

        return Result.success().put(Constants.DATA, clusterHost);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterHostEntity clusterHost) {
        clusterHostService.save(clusterHost);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterHostEntity clusterHost) {
        clusterHostService.updateById(clusterHost);

        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(Integer hostId) {

        return clusterHostService.deleteHost(hostId);

    }

}
