package com.datasophon.api.service.impl;

import com.datasophon.api.service.FrameServiceService;
import com.datasophon.common.utils.CollectionUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.FrameServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<FrameInfoEntity> frameInfoEntities = this.list();
        if(CollectionUtils.isEmpty(frameInfoEntities)) {
            return Result.success();
        }

        Set<Integer> frameInfoIds = frameInfoEntities.stream().map(FrameInfoEntity::getId).collect(Collectors.toSet());
        Map<Integer, List<FrameServiceEntity>> frameServiceGroupBys = frameServiceService.lambdaQuery()
                .in(FrameServiceEntity::getFrameId, frameInfoIds)
                .list()
                .stream()
                .collect(Collectors.groupingBy(FrameServiceEntity::getFrameId));
        frameInfoEntities.forEach(f -> f.setFrameServiceList(frameServiceGroupBys.get(f.getId())));

        return Result.success(frameInfoEntities);
    }
}
