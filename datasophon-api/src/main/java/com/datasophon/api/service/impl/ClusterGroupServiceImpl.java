package com.datasophon.api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datasophon.api.service.ClusterGroupService;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.service.ClusterUserGroupService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterGroup;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.mapper.ClusterGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("clusterGroupService")
public class ClusterGroupServiceImpl extends ServiceImpl<ClusterGroupMapper, ClusterGroup> implements ClusterGroupService {

    @Autowired
    private ClusterHostService hostService;

    @Autowired
    private ClusterUserGroupService userGroupService;

    @Override
    public Result saveClusterGroup(Integer clusterId, String groupName)  {
        //判读groupName是否重复
        if(hasRepeatGroupName(clusterId,groupName)){
            return Result.error("组名重复");
        }
        ClusterGroup clusterGroup = new ClusterGroup();
        clusterGroup.setClusterId(clusterId);
        clusterGroup.setGroupName(groupName);
        List<ClusterHostEntity> hostList = hostService.getHostListByClusterId(clusterId);
        ProcessUtils.syncUserGroupToHosts(hostList, clusterGroup.getGroupName(),"groupadd");
        this.save(clusterGroup);
        return Result.success();
    }

    private boolean hasRepeatGroupName(Integer clusterId, String groupName) {
        List<ClusterGroup> list = this.list(new QueryWrapper<ClusterGroup>()
                .eq(Constants.CLUSTER_ID, clusterId)
                .eq(Constants.GROUP_NAME, groupName));
        if(list.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public void refreshUserGroupToHost(Integer clusterId) {
        List<ClusterHostEntity> hostList = hostService.getHostListByClusterId(clusterId);
        List<ClusterGroup> groupList = this.list();
        for (ClusterGroup clusterGroup : groupList) {
            ProcessUtils.syncUserGroupToHosts(hostList, clusterGroup.getGroupName(),"groupadd");
        }
    }



    @Override
    public Result deleteUserGroup(Integer id) {
        ClusterGroup clusterGroup = this.getById(id);
        Integer num = userGroupService.countGroupUserNum(id);
        if(num > 0){
            return Result.error("当前用户组存在用户，请先删除用户");
        }
        this.removeById(id);
        List<ClusterHostEntity> hostList = hostService.getHostListByClusterId(clusterGroup.getClusterId());
        ProcessUtils.syncUserGroupToHosts(hostList,clusterGroup.getGroupName(),"groupdel");
        return Result.success();
    }

    @Override
    public Result listPage(String groupName, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        List<ClusterGroup> list = this.list(new QueryWrapper<ClusterGroup>().like(Constants.GROUP_NAME, groupName)
                .last("limit " + offset + "," + pageSize));
        int total = this.count(new QueryWrapper<ClusterGroup>().like(Constants.GROUP_NAME, groupName));
        return Result.success(list).put(Constants.TOTAL,total);
    }
}
