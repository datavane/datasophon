package com.datasophon.api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datasophon.api.service.ClusterGroupService;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.service.ClusterUserGroupService;
import com.datasophon.api.service.ClusterUserService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterGroup;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterUser;
import com.datasophon.dao.entity.ClusterUserGroup;
import com.datasophon.dao.mapper.ClusterUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;





@Service("clusterUserService")
public class ClusterUserServiceImpl extends ServiceImpl<ClusterUserMapper, ClusterUser> implements ClusterUserService {

    @Autowired
    private ClusterGroupService groupService;

    @Autowired
    private ClusterHostService hostService;

    @Autowired
    private ClusterUserGroupService userGroupService;

    @Override
    public Result create(Integer clusterId , String username, String groupIds) {
        if(hasRepeatUserName(clusterId,username)){
            return Result.error("用户名重复");
        }
        List<ClusterHostEntity> hostList = hostService.getHostListByClusterId(clusterId);

        ClusterUser clusterUser = new ClusterUser();
        clusterUser.setUsername(username);
        clusterUser.setClusterId(clusterId);

        List<Integer> ids = Arrays.stream(groupIds.split(",")).map(e -> Integer.parseInt(e)).collect(Collectors.toList());
        this.save(clusterUser);
        Integer mainGroupId = ids.get(0);
        for (Integer id : ids) {
            ClusterUserGroup clusterUserGroup = new ClusterUserGroup();
            clusterUserGroup.setUserId(clusterUser.getId());
            clusterUserGroup.setGroupId(id);
            clusterUserGroup.setClusterId(clusterId);
            if(id == mainGroupId){
                clusterUserGroup.setUserGroupType(1);
            }else {
                clusterUserGroup.setUserGroupType(2);
            }
            userGroupService.save(clusterUserGroup);
        }

        ClusterGroup mainGroup = groupService.getById(mainGroupId);
        ids.remove(0);
        Collection<ClusterGroup> clusterGroups = groupService.listByIds(ids);
        String otherGroup = clusterGroups.stream().map(e -> e.getGroupName()).collect(Collectors.joining(","));
        ProcessUtils.syncUserToHosts(hostList,username,mainGroup.getGroupName(),otherGroup,"useradd");

        return Result.success();
    }

    private boolean hasRepeatUserName(Integer clusterId, String username) {
        List<ClusterUser> list = this.list(new QueryWrapper<ClusterUser>()
                .eq(Constants.CLUSTER_ID, clusterId)
                .eq(Constants.USERNAME, username));
        if(list.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public Result listPage(Integer clusterId ,String username, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        List<ClusterUser> list = this.list(new QueryWrapper<ClusterUser>().like(Constants.USERNAME, username)
                .last("limit " + offset + "," + pageSize));
        int total = this.count(new QueryWrapper<ClusterUser>().like(Constants.USERNAME, username));
        return Result.success(list).put(Constants.TOTAL,total);
    }

    @Override
    public Result deleteClusterUser(Integer id) {
        ClusterUser clusterUser = this.getById(id);
        //delete user and group
        userGroupService.deleteByUser(id);
        List<ClusterHostEntity> hostList = hostService.getHostListByClusterId(clusterUser.getClusterId());
        ProcessUtils.syncUserToHosts(hostList,clusterUser.getUsername(),"","","userdel");
        this.removeById(id);
        return Result.success();
    }
}
