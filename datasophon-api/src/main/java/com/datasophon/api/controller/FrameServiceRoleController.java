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

import com.datasophon.api.service.AlertGroupService;
import com.datasophon.api.service.FrameServiceRoleService;
import com.datasophon.dao.entity.AlertGroupEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.datasophon.dao.entity.FrameServiceRoleEntity;
import com.datasophon.common.utils.Result;


/**
 * 框架服务角色表
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-04-18 14:38:53
 */
@Api(tags = "框架服务角色")
@RestController
@RequestMapping("api/frame/service/role")
public class FrameServiceRoleController {
    @Autowired
    private FrameServiceRoleService frameServiceRoleService;

    @Autowired
    private AlertGroupService alertGroupService;

    /**
     * 查询服务对应的角色列表
     */
    @ApiOperation(value = "查询服务对应的角色列表")
    @PostMapping("/getServiceRoleList")
    public Result getServiceRoleOfMaster(Integer clusterId,String serviceIds,Integer serviceRoleType){
        return frameServiceRoleService.getServiceRoleList(clusterId,serviceIds,serviceRoleType);
    }

    @ApiOperation(value = "获取非主角色列表")
    @PostMapping("/getNonMasterRoleList")
    public Result getNonMasterRoleList(Integer clusterId,String serviceIds){
        return frameServiceRoleService.getNonMasterRoleList(clusterId,serviceIds);
    }

    @ApiOperation(value = "按服务名称获取服务角色")
    @PostMapping("/getServiceRoleByServiceName")
    public Result getServiceRoleByServiceName(Integer clusterId,Integer alertGroupId){
        AlertGroupEntity alertGroupEntity = alertGroupService.getById(alertGroupId);
        return frameServiceRoleService.getServiceRoleByServiceName(clusterId,alertGroupEntity.getAlertGroupCategory());
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        FrameServiceRoleEntity frameServiceRole = frameServiceRoleService.getById(id);

        return Result.success().put("frameServiceRole", frameServiceRole);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result save(@RequestBody FrameServiceRoleEntity frameServiceRole){
        frameServiceRoleService.save(frameServiceRole);

        return Result.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody FrameServiceRoleEntity frameServiceRole){
        frameServiceRoleService.updateById(frameServiceRole);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        frameServiceRoleService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
