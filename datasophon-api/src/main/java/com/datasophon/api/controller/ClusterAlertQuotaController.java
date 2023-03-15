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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterAlertQuota;
import com.datasophon.api.service.ClusterAlertQuotaService;

/**
 * 集群告警指标表 
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-06-24 15:10:41
 */
@Api(tags = "集群告警指标")
@RestController
@RequestMapping("cluster/alert/quota")
public class ClusterAlertQuotaController {
    @Autowired
    private ClusterAlertQuotaService clusterAlertQuotaService;


    /**
     * 信息
     */
    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public Result info(Integer clusterId,Integer alertGroupId,String quotaName,Integer page,Integer pageSize){
        return clusterAlertQuotaService.getAlertQuotaList(clusterId,alertGroupId,quotaName,page,pageSize);
    }

    /**
     * 启用
     */
    @ApiOperation(value = "启用")
    @PostMapping("/start")
    public Result start(Integer clusterId,String alertQuotaIds){
        return clusterAlertQuotaService.start(clusterId,alertQuotaIds);
    }

    /**
     * 停用
     */
    @ApiOperation(value = "停用")
    @PostMapping("/stop")
    public Result stop(Integer clusterId,String alertQuotaIds){
        return clusterAlertQuotaService.stop(clusterId,alertQuotaIds);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterAlertQuota clusterAlertQuota){


        clusterAlertQuotaService.saveAlertQuota(clusterAlertQuota);
        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterAlertQuota clusterAlertQuota){

        clusterAlertQuotaService.updateById(clusterAlertQuota);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        clusterAlertQuotaService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
