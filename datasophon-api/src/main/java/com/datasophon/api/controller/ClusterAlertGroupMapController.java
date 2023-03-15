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
import org.springframework.web.bind.annotation.*;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterAlertGroupMap;
import com.datasophon.api.service.ClusterAlertGroupMapService;

/**
 * 
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-07-13 10:01:06
 */
@Api(tags = "告警组")
@RestController
@RequestMapping("cluster/alert/group/map")
public class ClusterAlertGroupMapController {
    @Autowired
    private ClusterAlertGroupMapService clusterAlertGroupMapService;

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
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterAlertGroupMap clusterAlertGroupMap = clusterAlertGroupMapService.getById(id);

        return Result.success().put("clusterAlertGroupMap", clusterAlertGroupMap);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody ClusterAlertGroupMap clusterAlertGroupMap){
        clusterAlertGroupMapService.save(clusterAlertGroupMap);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody ClusterAlertGroupMap clusterAlertGroupMap){

        clusterAlertGroupMapService.updateById(clusterAlertGroupMap);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        clusterAlertGroupMapService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
