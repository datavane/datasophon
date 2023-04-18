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

package com.datasophon.api.service.impl;

import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.service.FrameServiceRoleService;
import com.datasophon.api.service.FrameServiceService;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.entity.FrameServiceEntity;
import com.datasophon.dao.entity.FrameServiceRoleEntity;
import com.datasophon.dao.enums.RoleType;
import com.datasophon.dao.mapper.FrameServiceRoleMapper;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("frameServiceRoleService")
public class FrameServiceRoleServiceImpl extends ServiceImpl<FrameServiceRoleMapper, FrameServiceRoleEntity>
        implements
            FrameServiceRoleService {

    @Autowired
    private ClusterInfoService clusterInfoService;

    @Autowired
    private ClusterServiceRoleInstanceService roleInstanceService;

    @Autowired
    private FrameServiceService frameService;

    @Override
    public Result getServiceRoleList(Integer clusterId, String serviceIds, Integer serviceRoleType) {
        List<String> ids = Arrays.asList(serviceIds.split(","));
        List<FrameServiceRoleEntity> list = this.lambdaQuery()
                .eq(Objects.nonNull(serviceRoleType), FrameServiceRoleEntity::getServiceRoleType, serviceRoleType)
                .in(FrameServiceRoleEntity::getServiceId, ids)
                .list();
        // 校验是否已安装依赖的服务
        // 校验是否已安装Prometheus,Grafana,AlertManager
        ClusterInfoEntity clusterInfo = clusterInfoService.getById(clusterId);
        String key = clusterInfo.getClusterCode() + Constants.UNDERLINE + Constants.SERVICE_ROLE_HOST_MAPPING;

        for (FrameServiceRoleEntity role : list) {
            FrameServiceEntity frameServiceEntity = frameService.getById(role.getServiceId());
            List<ClusterServiceRoleInstanceEntity> roleInstanceList =
                    roleInstanceService.list(new QueryWrapper<ClusterServiceRoleInstanceEntity>()
                            .eq(Constants.SERVICE_NAME, frameServiceEntity.getServiceName())
                            .eq(Constants.SERVICE_ROLE_NAME, role.getServiceRoleName())
                            .eq(Constants.CLUSTER_ID, clusterId));
            if (Objects.nonNull(roleInstanceList) && roleInstanceList.size() > 0) {
                List<String> hosts = roleInstanceList.stream().map(e -> e.getHostname()).collect(Collectors.toList());
                role.setHosts(hosts);
            } else if (CacheUtils.constainsKey(key)) {
                Map<String, List<String>> map = (Map<String, List<String>>) CacheUtils.get(key);
                if (map.containsKey(role.getServiceRoleName())) {
                    role.setHosts(map.get(role.getServiceRoleName()));
                }
            }
        }
        return Result.success(list);
    }

    @Override
    public FrameServiceRoleEntity getServiceRoleByServiceIdAndServiceRoleName(Integer serviceId, String roleName) {
        return this.lambdaQuery()
                .eq(FrameServiceRoleEntity::getServiceId, serviceId)
                .eq(FrameServiceRoleEntity::getServiceRoleName, roleName)
                .one();
    }

    @Override
    public FrameServiceRoleEntity getServiceRoleByFrameCodeAndServiceRoleName(String clusterFrame,
                                                                              String serviceRoleName) {
        return this.getOne(new QueryWrapper<FrameServiceRoleEntity>()
                .eq(Constants.FRAME_CODE_1, clusterFrame).eq(Constants.SERVICE_ROLE_NAME, serviceRoleName));
    }

    @Override
    public Result getNonMasterRoleList(Integer clusterId, String serviceIds) {
        List<String> ids = Arrays.asList(serviceIds.split(","));
        List<FrameServiceRoleEntity> list = this.lambdaQuery()
                .ne(FrameServiceRoleEntity::getServiceRoleType, RoleType.MASTER)
                .in(FrameServiceRoleEntity::getServiceId, ids)
                .list();
        ClusterInfoEntity clusterInfo = clusterInfoService.getById(clusterId);
        String key = clusterInfo.getClusterCode() + Constants.UNDERLINE + Constants.SERVICE_ROLE_HOST_MAPPING;
        List<String> hosts = new ArrayList<>();
        for (FrameServiceRoleEntity role : list) {
            FrameServiceEntity frameServiceEntity = frameService.getById(role.getServiceId());
            List<ClusterServiceRoleInstanceEntity> roleInstanceList =
                    roleInstanceService.list(new QueryWrapper<ClusterServiceRoleInstanceEntity>()
                            .eq(Constants.SERVICE_NAME, frameServiceEntity.getServiceName())
                            .eq(Constants.SERVICE_ROLE_NAME, role.getServiceRoleName())
                            .eq(Constants.CLUSTER_ID, clusterId));
            if (Objects.nonNull(roleInstanceList) && roleInstanceList.size() > 0) {
                hosts = roleInstanceList.stream().map(e -> e.getHostname()).collect(Collectors.toList());

            } else if (CacheUtils.constainsKey(key)) {
                Map<String, List<String>> map = (Map<String, List<String>>) CacheUtils.get(key);
                if (map.containsKey(role.getServiceRoleName())) {
                    hosts = map.get(role.getServiceRoleName());
                }
            }
            role.setHosts(hosts);
        }
        return Result.success(list);
    }

    @Override
    public Result getServiceRoleByServiceName(Integer clusterId, String serviceName) {
        if ("NODE".equals(serviceName)) {
            List<FrameServiceRoleEntity> list = new ArrayList<>();
            FrameServiceRoleEntity frameServiceRoleEntity = new FrameServiceRoleEntity();
            frameServiceRoleEntity.setServiceRoleName("node");
            list.add(frameServiceRoleEntity);
            return Result.success(list);
        }
        ClusterInfoEntity clusterInfoEntity = clusterInfoService.getById(clusterId);
        FrameServiceEntity frameServiceEntity =
                frameService.getServiceByFrameCodeAndServiceName(clusterInfoEntity.getClusterFrame(), serviceName);
        List<FrameServiceRoleEntity> list = this.lambdaQuery()
                .eq(FrameServiceRoleEntity::getServiceId, frameServiceEntity.getId())
                .list();
        return Result.success(list);
    }

    @Override
    public List<FrameServiceRoleEntity> getAllServiceRoleList(Integer frameServiceId) {
        return this.lambdaQuery().eq(FrameServiceRoleEntity::getServiceId, frameServiceId).list();
    }

}
