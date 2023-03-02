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

import com.datasophon.api.service.ClusterAlertGroupMapService;
import com.datasophon.api.service.ClusterAlertQuotaService;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterAlertGroupMap;
import com.datasophon.dao.entity.ClusterAlertQuota;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.AlertGroupMapper;
import com.datasophon.dao.entity.AlertGroupEntity;
import com.datasophon.api.service.AlertGroupService;


@Service("alertGroupService")
public class AlertGroupServiceImpl extends ServiceImpl<AlertGroupMapper, AlertGroupEntity> implements AlertGroupService {

    @Autowired
    private ClusterAlertGroupMapService alertGroupMapService;

    @Autowired
    private ClusterAlertQuotaService quotaService;

    @Override
    public Result getAlertGroupList(Integer clusterId, String alertGroupName, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        List<ClusterAlertGroupMap> list = alertGroupMapService.list(new QueryWrapper<ClusterAlertGroupMap>().eq(Constants.CLUSTER_ID, clusterId));
        List<Integer> groupIds = list.stream().map(e -> e.getAlertGroupId()).collect(Collectors.toList());
        List<AlertGroupEntity> alertGroupList = this.list(new QueryWrapper<AlertGroupEntity>()
                .in(Constants.ID, groupIds)
                .like(StringUtils.isNotBlank(alertGroupName),Constants.ALERT_GROUP_NAME,alertGroupName)
                .last("limit " + offset + "," + pageSize));
        int count = this.count(new QueryWrapper<AlertGroupEntity>()
                .in(Constants.ID, groupIds)
                .like(StringUtils.isNotBlank(alertGroupName),Constants.ALERT_GROUP_NAME,alertGroupName));
        //查询告警组下告警指标个数
        for (AlertGroupEntity alertGroupEntity : alertGroupList) {
            List<ClusterAlertQuota> quotaList = quotaService.list(new QueryWrapper<ClusterAlertQuota>().eq(Constants.ALERT_GROUP_ID, alertGroupEntity.getId()));
            alertGroupEntity.setAlertQuotaNum(quotaList.size());
        }
        return Result.success(alertGroupList).put(Constants.TOTAL,count);
    }

    @Override
    public Result saveAlertGroup(AlertGroupEntity alertGroup) {
        this.save(alertGroup);
        ClusterAlertGroupMap clusterAlertGroupMap = new ClusterAlertGroupMap();
        clusterAlertGroupMap.setAlertGroupId(alertGroup.getId());
        clusterAlertGroupMap.setClusterId(alertGroup.getClusterId());
        alertGroupMapService.save(clusterAlertGroupMap);
        return Result.success();
    }
}
