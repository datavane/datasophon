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
import com.datasophon.api.service.ClusterServiceInstanceRoleGroupService;
import com.datasophon.api.service.ClusterServiceInstanceService;
import com.datasophon.api.service.ClusterServiceRoleGroupConfigService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.utils.CollectionUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterServiceInstanceEntity;
import com.datasophon.dao.entity.ClusterServiceInstanceRoleGroup;
import com.datasophon.dao.entity.ClusterServiceRoleGroupConfig;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.NeedRestart;
import com.datasophon.dao.mapper.ClusterServiceInstanceRoleGroupMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("clusterServiceInstanceRoleGroupService")
public class ClusterServiceInstanceRoleGroupServiceImpl
        extends ServiceImpl<ClusterServiceInstanceRoleGroupMapper, ClusterServiceInstanceRoleGroup>
        implements ClusterServiceInstanceRoleGroupService {

    @Autowired private ClusterServiceInstanceService serviceInstanceService;

    @Autowired private ClusterServiceRoleInstanceService roleInstanceService;

    @Autowired private ClusterServiceRoleGroupConfigService roleGroupConfigService;

    @Override
    public ClusterServiceInstanceRoleGroup getRoleGroupByServiceInstanceId(
            Integer serviceInstanceId) {
        return this.lambdaQuery()
                .eq(ClusterServiceInstanceRoleGroup::getServiceInstanceId, serviceInstanceId)
                .eq(ClusterServiceInstanceRoleGroup::getRoleGroupType, "default")
                .one();
    }

    @Override
    public Result saveRoleGroup(
            Integer serviceInstanceId, Integer roleGroupId, String roleGroupName) {
        ClusterServiceInstanceEntity serviceInstance =
                serviceInstanceService.getById(serviceInstanceId);
        // is repeat name
        if (isRepeatRoleGroupName(serviceInstanceId, roleGroupName)) {
            return Result.error(Status.REPEAT_ROLE_GROUP_NAME.getMsg());
        }
        ClusterServiceInstanceRoleGroup roleGroup = new ClusterServiceInstanceRoleGroup();
        roleGroup.setRoleGroupType("custom");
        roleGroup.setRoleGroupName(roleGroupName);
        roleGroup.setServiceName(serviceInstance.getServiceName());
        roleGroup.setServiceInstanceId(serviceInstanceId);
        roleGroup.setClusterId(serviceInstance.getClusterId());
        this.save(roleGroup);
        ClusterServiceRoleGroupConfig config =
                roleGroupConfigService.getConfigByRoleGroupId(roleGroupId);
        ClusterServiceRoleGroupConfig roleGroupConfig = new ClusterServiceRoleGroupConfig();
        BeanUtils.copyProperties(config, roleGroupConfig);
        roleGroupConfig.setConfigVersion(1);
        roleGroupConfig.setId(null);
        roleGroupConfig.setRoleGroupId(roleGroup.getId());
        roleGroupConfigService.save(roleGroupConfig);
        return Result.success();
    }

    private boolean isRepeatRoleGroupName(Integer serviceInstanceId, String roleGroupName) {
        Integer count =
                this.lambdaQuery()
                        .eq(
                                ClusterServiceInstanceRoleGroup::getServiceInstanceId,
                                serviceInstanceId)
                        .eq(ClusterServiceInstanceRoleGroup::getRoleGroupName, roleGroupName)
                        .count();
        return null != count && count > 0;
    }

    @Override
    public void bind(String roleInstanceIds, Integer roleGroupId) {
        String[] ids = roleInstanceIds.split(",");
        ArrayList<ClusterServiceRoleInstanceEntity> list = new ArrayList<>();
        for (String id : ids) {
            ClusterServiceRoleInstanceEntity roleInstanceEntity = roleInstanceService.getById(id);
            // 判断新角色组与原角色组配置是否相同，不相同则需标识该角色实例需要重启
            if (!isSameConfig(roleInstanceEntity.getRoleGroupId(), roleGroupId)) {
                roleInstanceEntity.setNeedRestart(NeedRestart.YES);
            }
            roleInstanceEntity.setRoleGroupId(roleGroupId);
            list.add(roleInstanceEntity);
        }
        roleInstanceService.updateBatchById(list);
    }

    private boolean isSameConfig(Integer oldRoleGroupId, Integer newRoleGroupId) {
        ClusterServiceRoleGroupConfig oldConfig =
                roleGroupConfigService.getConfigByRoleGroupId(oldRoleGroupId);
        ClusterServiceRoleGroupConfig newConfig =
                roleGroupConfigService.getConfigByRoleGroupId(newRoleGroupId);
        if (oldConfig.getConfigJsonMd5().equals(newConfig.getConfigJsonMd5())) {
            return true;
        }
        return false;
    }

    @Override
    public ClusterServiceRoleGroupConfig getRoleGroupConfigByServiceId(Integer serviceInstanceId) {
        ClusterServiceInstanceRoleGroup instanceRoleGroup =
                this.lambdaQuery()
                        .eq(
                                ClusterServiceInstanceRoleGroup::getServiceInstanceId,
                                serviceInstanceId)
                        .eq(ClusterServiceInstanceRoleGroup::getRoleGroupType, "default")
                        .one();
        return roleGroupConfigService.getConfigByRoleGroupId(instanceRoleGroup.getId());
    }

    @Override
    public Result rename(Integer roleGroupId, String roleGroupName) {
        ClusterServiceInstanceRoleGroup roleGroup = this.getById(roleGroupId);
        if (!roleGroup.getRoleGroupName().equals(roleGroupName)
                && isRepeatRoleGroupName(roleGroup.getServiceInstanceId(), roleGroupName)) {
            return Result.error(Status.REPEAT_ROLE_GROUP_NAME.getMsg());
        }
        roleGroup.setRoleGroupName(roleGroupName);
        this.updateById(roleGroup);
        return Result.success();
    }

    @Override
    public Result deleteRoleGroup(Integer roleGroupId) {
        if (hasRoleInstanceUse(roleGroupId)) {
            return Result.error(Status.THE_CURRENT_ROLE_GROUP_BE_USING.getMsg());
        }
        this.removeById(roleGroupId);
        roleGroupConfigService.removeAllByRoleGroupId(roleGroupId);
        return Result.success();
    }

    @Override
    public List<ClusterServiceInstanceRoleGroup> listRoleGroupByServiceInstanceId(
            Integer serviceInstanceId) {
        return this.lambdaQuery()
                .eq(ClusterServiceInstanceRoleGroup::getServiceInstanceId, serviceInstanceId)
                .list();
    }

    private boolean hasRoleInstanceUse(Integer roleGroupId) {
        List<ClusterServiceRoleInstanceEntity> list =
                roleInstanceService
                        .lambdaQuery()
                        .eq(ClusterServiceRoleInstanceEntity::getRoleGroupId, roleGroupId)
                        .list();
        return CollectionUtils.isNotEmpty(list);
    }
}
