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
 *
 */

package com.datasophon.api.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.service.AlertGroupService;
import com.datasophon.api.service.ClusterAlertQuotaService;
import com.datasophon.common.Constants;
import com.datasophon.dao.entity.ClusterAlertQuota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datasophon.dao.entity.AlertGroupEntity;
import com.datasophon.common.utils.Result;


/**
 * 告警组表
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-03-15 17:36:08
 */
@RestController
@RequestMapping("alert/group")
public class AlertGroupController {
    @Autowired
    private AlertGroupService alertGroupService;

    @Autowired
    private ClusterAlertQuotaService alertQuotaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(Integer clusterId,String alertGroupName,Integer page,Integer pageSize) {
       return alertGroupService.getAlertGroupList(clusterId,alertGroupName,page,pageSize);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        AlertGroupEntity alertGroup = alertGroupService.getById(id);

        return Result.success().put("alertGroup", alertGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody AlertGroupEntity alertGroup) {
        alertGroup.setCreateTime(new Date());
        return alertGroupService.saveAlertGroup(alertGroup);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody AlertGroupEntity alertGroup) {
        alertGroupService.updateById(alertGroup);

        return Result.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids) {

        //校验是否绑定告警指标
        List<ClusterAlertQuota> list = alertQuotaService.list(new QueryWrapper<ClusterAlertQuota>().in(Constants.ALERT_GROUP_ID, ids));
        if(list.size() > 0){
            return Result.error("当前告警组已绑定告警指标，请先删除绑定的告警指标");
        }
        alertGroupService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
