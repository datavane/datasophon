/*
 *
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
 *
 */

package com.datasophon.api.service.impl;

import com.datasophon.api.enums.Status;
import com.datasophon.api.master.ActorUtils;
import com.datasophon.api.master.RackActor;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.GenerateRackPropCommand;
import com.datasophon.common.utils.CollectionUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.RoleType;
import com.datasophon.dao.enums.ServiceRoleState;
import com.datasophon.dao.mapper.ClusterHostMapper;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import akka.actor.ActorRef;

@Service("clusterHostService")
@Transactional
public class ClusterHostServiceImpl extends ServiceImpl<ClusterHostMapper, ClusterHostEntity>
        implements ClusterHostService {

    @Autowired ClusterHostMapper hostMapper;

    @Autowired ClusterServiceRoleInstanceService roleInstanceService;

    @Autowired ClusterInfoService clusterInfoService;

    private final String IP = "ip";

    @Override
    public ClusterHostEntity getClusterHostByHostname(String hostname) {
        return hostMapper.getClusterHostByHostname(hostname);
    }

    @Override
    public Result listByPage(
            Integer clusterId,
            String hostname,
            String ip,
            String cpuArchitecture,
            Integer hostState,
            String orderField,
            String orderType,
            Integer page,
            Integer pageSize) {
        Integer offset = (page - 1) * pageSize;

        LambdaQueryChainWrapper<ClusterHostEntity> wrapper =
                this.lambdaQuery()
                        .eq(ClusterHostEntity::getClusterId, clusterId)
                        .eq(ClusterHostEntity::getManaged, 1)
                        .eq(
                                StringUtils.isNotBlank(cpuArchitecture),
                                ClusterHostEntity::getCpuArchitecture,
                                cpuArchitecture)
                        .eq(hostState != null, ClusterHostEntity::getHostState, hostState)
                        .like(StringUtils.isNotBlank(ip), ClusterHostEntity::getIp, ip)
                        .like(
                                StringUtils.isNotBlank(hostname),
                                ClusterHostEntity::getHostname,
                                hostname);
        if ("asc".equals(orderType)) {
            wrapper.last("ORDER BY " + orderField + "ASC");
        }
        if ("desc".equals(orderType)) {
            wrapper.last("ORDER BY " + orderField + "DESC");
        }
        List<ClusterHostEntity> list = wrapper.last("limit " + offset + "," + pageSize).list();
        if (CollectionUtils.isEmpty(list)) {
            return Result.successEmptyCount();
        }

        for (ClusterHostEntity clusterHostEntity : list) {
            // 查询主机上服务角色数
            Integer serviceRoleNum =
                    roleInstanceService
                            .lambdaQuery()
                            .eq(
                                    ClusterServiceRoleInstanceEntity::getHostname,
                                    clusterHostEntity.getHostname())
                            .count();
            clusterHostEntity.setServiceRoleNum(serviceRoleNum);
        }

        Integer count = wrapper.count();
        return Result.success(list).put(Constants.TOTAL, count);
    }

    @Override
    public List<ClusterHostEntity> getHostListByClusterId(Integer clusterId) {
        return this.lambdaQuery()
                .eq(ClusterHostEntity::getClusterId, clusterId)
                .eq(ClusterHostEntity::getManaged, 1)
                .list();
    }

    @Override
    public Result getRoleListByHostname(Integer clusterId, String hostname) {
        List<ClusterServiceRoleInstanceEntity> list =
                roleInstanceService.getServiceRoleListByHostnameAndClusterId(hostname, clusterId);
        for (ClusterServiceRoleInstanceEntity roleInstanceEntity : list) {
            roleInstanceEntity.setServiceRoleStateCode(
                    roleInstanceEntity.getServiceRoleState().getValue());
        }
        return Result.success(list);
    }

    @Override
    public Result deleteHost(Integer hostId) {
        ClusterHostEntity host = this.getById(hostId);
        List<ClusterServiceRoleInstanceEntity> list =
                roleInstanceService
                        .lambdaQuery()
                        .eq(ClusterServiceRoleInstanceEntity::getClusterId, host.getClusterId())
                        .eq(ClusterServiceRoleInstanceEntity::getHostname, host.getHostname())
                        .eq(
                                ClusterServiceRoleInstanceEntity::getServiceRoleState,
                                ServiceRoleState.RUNNING)
                        .ne(ClusterServiceRoleInstanceEntity::getRoleType, RoleType.CLIENT)
                        .list();
        List<String> roles =
                list.stream().map(e -> e.getServiceRoleName()).collect(Collectors.toList());
        if (Objects.nonNull(list) && list.size() > 0) {
            return Result.error(
                    host.getHostname()
                            + Status.HOST_EXIT_ONE_RUNNING_ROLE.getMsg()
                            + roles.toString());
        }
        ClusterInfoEntity clusterInfo = clusterInfoService.getById(host.getClusterId());
        String clusterCode = clusterInfo.getClusterCode();
        String distributeAgentKey =
                clusterCode + Constants.UNDERLINE + Constants.START_DISTRIBUTE_AGENT;
        if (CacheUtils.constainsKey(
                distributeAgentKey + Constants.UNDERLINE + host.getHostname())) {
            CacheUtils.removeKey(distributeAgentKey + Constants.UNDERLINE + host.getHostname());
        }
        this.removeById(hostId);
        return Result.success();
    }

    @Override
    public Result getRack(Integer clusterId) {
        ArrayList<JSONObject> list = new ArrayList<>();
        JSONObject rack = new JSONObject();
        rack.put("rack", "/default-rack");
        list.add(rack);
        return Result.success(list);
    }

    @Override
    public void deleteHostByClusterId(Integer clusterId) {
        this.lambdaUpdate().eq(ClusterHostEntity::getClusterId, clusterId).remove();
    }

    @Override
    public void updateBatchNodeLabel(List<String> hostIds, String nodeLabel) {
        List<ClusterHostEntity> list =
                this.lambdaQuery().in(ClusterHostEntity::getId, hostIds).list();
        for (ClusterHostEntity clusterHostEntity : list) {
            clusterHostEntity.setNodeLabel(nodeLabel);
        }
        this.updateBatchById(list);
    }

    @Override
    public List<ClusterHostEntity> getHostListByIds(List<String> ids) {
        return this.lambdaQuery().in(ClusterHostEntity::getId, ids).list();
    }

    @Override
    public Result assignRack(Integer clusterId, String rack, String hostIds) {
        List<String> ids = Arrays.asList(hostIds.split(","));
        List<ClusterHostEntity> list = this.lambdaQuery().in(ClusterHostEntity::getId, ids).list();
        for (ClusterHostEntity clusterHostEntity : list) {
            clusterHostEntity.setRack(rack);
        }
        this.updateBatchById(list);
        // tell rack actor
        GenerateRackPropCommand command = new GenerateRackPropCommand();
        command.setClusterId(clusterId);
        ActorRef rackActor = ActorUtils.getLocalActor(RackActor.class, "rackActor");
        rackActor.tell(command, ActorRef.noSender());
        return Result.success();
    }

    @Override
    public List<ClusterHostEntity> getClusterHostByRack(Integer clusterId, String rack) {
        return this.lambdaQuery()
                .eq(ClusterHostEntity::getClusterId, clusterId)
                .eq(ClusterHostEntity::getRack, rack)
                .list();
    }
}
