package com.datasophon.api.service.impl;

import com.datasophon.common.Constants;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.ClusterServiceRoleGroupConfigMapper;
import com.datasophon.dao.entity.ClusterServiceRoleGroupConfig;
import com.datasophon.api.service.ClusterServiceRoleGroupConfigService;


@Service("clusterServiceRoleGroupConfigService")
public class ClusterServiceRoleGroupConfigServiceImpl extends ServiceImpl<ClusterServiceRoleGroupConfigMapper, ClusterServiceRoleGroupConfig> implements ClusterServiceRoleGroupConfigService {

    @Override
    public ClusterServiceRoleGroupConfig getConfigByRoleGroupId(Integer roleGroupId) {
        return this.getOne(new QueryWrapper<ClusterServiceRoleGroupConfig>()
                .eq(Constants.ROLE_GROUP_ID,roleGroupId).orderByDesc(Constants.CONFIG_VERSION).last("limit 1"));
    }

    @Override
    public ClusterServiceRoleGroupConfig getConfigByRoleGroupIdAndVersion(Integer roleGroupId, Integer version) {
        return this.getOne(new QueryWrapper<ClusterServiceRoleGroupConfig>()
                .eq(Constants.ROLE_GROUP_ID,roleGroupId).eq(Constants.CONFIG_VERSION,version));
    }
}
