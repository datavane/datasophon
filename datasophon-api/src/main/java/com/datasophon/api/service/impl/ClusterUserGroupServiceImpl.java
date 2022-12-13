package com.datasophon.api.service.impl;

import com.datasophon.common.Constants;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.ClusterUserGroupMapper;
import com.datasophon.dao.entity.ClusterUserGroup;
import com.datasophon.api.service.ClusterUserGroupService;


@Service("clusterUserGroupService")
public class ClusterUserGroupServiceImpl extends ServiceImpl<ClusterUserGroupMapper, ClusterUserGroup> implements ClusterUserGroupService {

    @Override
    public Integer countGroupUserNum(Integer groupId) {
        int count = this.count(new QueryWrapper<ClusterUserGroup>().eq(Constants.GROUP_ID, groupId));
        return count;
    }

    @Override
    public void deleteByUser(Integer id) {
        this.remove(new QueryWrapper<ClusterUserGroup>().eq(Constants.USER_ID,id));
    }
}
