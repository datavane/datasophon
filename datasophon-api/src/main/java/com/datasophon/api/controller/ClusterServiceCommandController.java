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

import cn.hutool.core.util.EnumUtil;
import com.datasophon.api.enums.Status;
import com.datasophon.api.service.ClusterServiceCommandService;
import com.datasophon.api.security.UserPermission;
import com.datasophon.common.enums.CommandType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.datasophon.dao.entity.ClusterServiceCommandEntity;
import com.datasophon.common.utils.Result;


/**
 * 集群服务操作指令表
 *
 * @author dygao2
 * @email gaodayu2022@163.com
 * @date 2022-04-12 11:28:06
 */
@Api(tags = "集群服务操作指令")
@RestController
@RequestMapping("api/cluster/service/command")
public class ClusterServiceCommandController {
    @Autowired
    private ClusterServiceCommandService clusterServiceCommandService;

    /**
     * 查询集群服务指令列表
     */
    @ApiOperation(value = "查询集群服务指令列表")
    @PostMapping("/getServiceCommandlist")
    public Result list(Integer clusterId, Integer page, Integer pageSize) {
        return clusterServiceCommandService.getServiceCommandlist(clusterId, page, pageSize);
    }


    /**
     * 生成服务安装操作指令
     */
    @ApiOperation(value = "生成服务安装操作指令")
    @UserPermission
    @PostMapping("/generateCommand")
    public Result generateCommand(Integer clusterId, String commandType, String serviceNames) {
        CommandType command = EnumUtil.fromString(CommandType.class, commandType);
        List<String> list = Arrays.asList(serviceNames.split(","));
        return clusterServiceCommandService.generateCommand(clusterId, command, list);
    }

    /**
     * 生成服务实例操作指令
     */
    @ApiOperation(value = "生成服务实例操作指令")
    @PostMapping("/generateServiceCommand")
    @UserPermission
    public Result generateServiceCommand(Integer clusterId, String commandType, String serviceInstanceIds) {
        CommandType command = EnumUtil.fromString(CommandType.class, commandType);
        if(StringUtils.isNotBlank(serviceInstanceIds)){
            List<String> ids = Arrays.asList(serviceInstanceIds.split(","));
            return clusterServiceCommandService.generateServiceCommand(clusterId, command, ids);
        }else {
            return Result.error(Status.NO_SERVICE_EXECUTE.getMsg());
        }


    }

    /**
     * 生成服务角色实例操作指令
     */
    @ApiOperation(value = "生成服务角色实例操作指令")
    @PostMapping("/generateServiceRoleCommand")
    @UserPermission
    public Result generateServiceRoleCommand(Integer clusterId, String commandType, Integer serviceInstanceId, String serviceRoleInstancesIds) {
        CommandType command = EnumUtil.fromString(CommandType.class, commandType);
        List<String> ids = Arrays.asList(serviceRoleInstancesIds.split(","));
        return clusterServiceCommandService.generateServiceRoleCommand(clusterId, command, serviceInstanceId, ids);

    }

    /**
     * 启动执行指令
     */
    @ApiOperation(value = "启动执行指令")
    @PostMapping("/startExecuteCommand")
    @UserPermission
    public Result startExecuteCommand(Integer clusterId, String commandType, String commandIds) {
        clusterServiceCommandService.startExecuteCommand(clusterId,commandType,commandIds);
        return Result.success();
    }

    @ApiOperation(value = "取消命令")
    @PostMapping("/cancelCommand")
    public Result cancelCommand(String commandId) {
        clusterServiceCommandService.cancelCommand(commandId);
        return Result.success();
    }



    /**
     * 信息
     */
    @PostMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        ClusterServiceCommandEntity clusterServiceCommand = clusterServiceCommandService.getById(id);

        return Result.success().put("clusterServiceCommand", clusterServiceCommand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody ClusterServiceCommandEntity clusterServiceCommand) {
        clusterServiceCommandService.save(clusterServiceCommand);

        return Result.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody ClusterServiceCommandEntity clusterServiceCommand) {
        clusterServiceCommandService.updateById(clusterServiceCommand);

        return Result.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {
        clusterServiceCommandService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }


}
