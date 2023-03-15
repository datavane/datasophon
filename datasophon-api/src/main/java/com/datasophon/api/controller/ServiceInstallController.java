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

import com.alibaba.fastjson.JSONArray;
import com.datasophon.api.service.ServiceInstallService;
import com.datasophon.api.security.UserPermission;
import com.datasophon.common.model.HostServiceRoleMapping;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleHostMapping;
import com.datasophon.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "服务安装")
@RestController
@RequestMapping("service/install")
public class ServiceInstallController {
    @Autowired
    ServiceInstallService serviceInstallService;

    /**
     * 根据服务名称查询服务配置选项
     */
    @ApiOperation(value = "根据服务名称查询服务配置选项")
    @PostMapping("/getServiceConfigOption")
    public Result getServiceConfigOption(Integer clusterId, String serviceName) {
        return serviceInstallService.getServiceConfigOption(clusterId, serviceName);
    }

    /**
     * 保存服务配置
     */
    @ApiOperation(value = "保存服务配置")
    @PostMapping("/saveServiceConfig")
    @UserPermission
    public Result saveServiceConfig(Integer clusterId, String serviceName,String serviceConfig,Integer roleGroupId) {
        JSONArray jsonArray = JSONArray.parseArray(serviceConfig);
        List<ServiceConfig> list = jsonArray.toJavaList(ServiceConfig.class);
        return serviceInstallService.saveServiceConfig(clusterId,serviceName,list,roleGroupId);

    }

    /**
     * 保存服务角色与主机对应关系
     */
    @ApiOperation(value = "保存服务角色与主机对应关系")
    @PostMapping("/saveServiceRoleHostMapping/{clusterId}")
    public Result saveServiceRoleHostMapping(@RequestBody List<ServiceRoleHostMapping> list, @PathVariable("clusterId") Integer clusterId) {
        return serviceInstallService.saveServiceRoleHostMapping(clusterId, list);
    }

    /**
     * 查询服务角色与主机对应关系
     */
    @ApiOperation(value = "查询服务角色与主机对应关系")
    @PostMapping("/getServiceRoleHostMapping")
    @UserPermission
    public Result getServiceRoleHostMapping( Integer clusterId) {
        return serviceInstallService.getServiceRoleHostMapping(clusterId);
    }

    /**
     * 保存主机与服务角色对应关系
     */
    @ApiOperation(value = "保存主机与服务角色对应关系")
    @PostMapping("/saveHostServiceRoleMapping/{clusterId}")
    public Result saveHostServiceRoleMapping(@PathVariable("clusterId")Integer clusterId, @RequestBody List<HostServiceRoleMapping> list) {

        return serviceInstallService.saveHostServiceRoleMapping(clusterId, list);
    }
    /**
     * 服务部署总览
     */
    @ApiOperation(value = "服务部署总览")
    @PostMapping("/getServiceRoleDeployOverview")
    public Result getServiceRoleDeployOverview(Integer clusterId) {
        return serviceInstallService.getServiceRoleDeployOverview(clusterId);
    }

    /**
     * 开始安装服务
     */
    @ApiOperation(value = "开始安装服务")
    @PostMapping("/startInstallService/{clusterId}")
    public Result startInstallService(@PathVariable("clusterId")Integer clusterId,@RequestBody List<String> commandIds) {

        return serviceInstallService.startInstallService(clusterId,commandIds);
    }

    /**
     * 下载安装包
     */
    @ApiOperation(value = "下载安装包")
    @GetMapping("/downloadPackage")
    public void downloadPackage(String packageName, String cpuArchitecture,HttpServletResponse response) throws IOException {

        serviceInstallService.downloadPackage(packageName,response);
    }


    /**
     * 服务部署总览
     */
    @ApiOperation(value = "服务部署总览")
    @PostMapping("/checkServiceDependency")
    public Result checkServiceDependency(Integer clusterId,String serviceIds) {
        return serviceInstallService.checkServiceDependency(clusterId,serviceIds);
    }

}
