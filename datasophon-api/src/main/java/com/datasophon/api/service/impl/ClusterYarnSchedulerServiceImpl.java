package com.datasophon.api.service.impl;

import com.datasophon.common.Constants;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.ClusterYarnSchedulerMapper;
import com.datasophon.dao.entity.ClusterYarnScheduler;
import com.datasophon.api.service.ClusterYarnSchedulerService;


@Service("clusterYarnSchedulerService")
public class ClusterYarnSchedulerServiceImpl extends ServiceImpl<ClusterYarnSchedulerMapper, ClusterYarnScheduler> implements ClusterYarnSchedulerService {

    @Override
    public ClusterYarnScheduler getScheduler(Integer clusterId) {
        return this.getOne(new QueryWrapper<ClusterYarnScheduler>().eq(Constants.CLUSTER_ID,clusterId));
    }

    @Override
    public void createYarnScheduler(Integer clusterId) {
        ClusterYarnScheduler scheduler = new ClusterYarnScheduler();
        scheduler.setScheduler("capacity");
        scheduler.setClusterId(clusterId);
        this.save(scheduler);
    }
}
