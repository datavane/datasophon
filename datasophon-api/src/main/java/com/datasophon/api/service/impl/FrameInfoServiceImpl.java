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

package com.datasophon.api.service.impl;

import com.datasophon.api.service.FrameServiceService;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.FrameServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.FrameInfoMapper;
import com.datasophon.dao.entity.FrameInfoEntity;
import com.datasophon.api.service.FrameInfoService;


@Service("frameInfoService")
public class FrameInfoServiceImpl extends ServiceImpl<FrameInfoMapper, FrameInfoEntity> implements FrameInfoService {

    @Autowired
    private FrameServiceService frameServiceService;

    @Override
    public Result getAllClusterFrame() {
        List<FrameInfoEntity> list = this.list();
        for (FrameInfoEntity frameInfo : list) {
            List<FrameServiceEntity> frameServiceList = frameServiceService.list(new QueryWrapper<FrameServiceEntity>().eq(Constants.FRAME_ID, frameInfo.getId()));
            frameInfo.setFrameServiceList(frameServiceList);
        }
        return Result.success(list);
    }
}
