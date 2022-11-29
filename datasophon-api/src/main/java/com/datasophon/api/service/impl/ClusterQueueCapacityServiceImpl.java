package com.datasophon.api.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.ClusterQueueCapacityMapper;
import com.datasophon.dao.entity.ClusterQueueCapacity;
import com.datasophon.api.service.ClusterQueueCapacityService;


@Service("clusterQueueCapacityService")
public class ClusterQueueCapacityServiceImpl extends ServiceImpl<ClusterQueueCapacityMapper, ClusterQueueCapacity> implements ClusterQueueCapacityService {

}
