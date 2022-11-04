package com.datasophon.api.service.impl;

import akka.actor.*;
import com.datasophon.api.configuration.ConfigBean;
import com.datasophon.api.enums.Status;
import com.datasophon.api.master.ActorUtils;
import com.datasophon.api.master.ServiceActor;
import com.datasophon.api.service.*;
import com.datasophon.dao.entity.*;
import com.datasophon.api.service.*;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SecurityUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.enums.ClusterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.datasophon.dao.mapper.ClusterInfoMapper;
import org.springframework.transaction.annotation.Transactional;


@Service("clusterInfoService")
@Transactional
public class ClusterInfoServiceImpl extends ServiceImpl<ClusterInfoMapper, ClusterInfoEntity> implements ClusterInfoService {

    @Autowired
    private ClusterInfoMapper clusterInfoMapper;

    @Autowired
    private ClusterRoleUserService clusterUserService;

    @Autowired
    private AlertGroupService alertGroupService;

    @Autowired
    private ClusterAlertGroupMapService groupMapService;

    @Autowired
    private ConfigBean configBean;

    @Autowired
    private FrameServiceService frameServiceService;

    @Override
    public ClusterInfoEntity getClusterByClusterCode(String clusterCode) {
        ClusterInfoEntity clusterInfoEntity = clusterInfoMapper.getClusterByClusterCode(clusterCode);
        return clusterInfoEntity;
    }

    @Override
    public Result saveCluster(ClusterInfoEntity clusterInfo) {
        //集群编码判重
        List<ClusterInfoEntity> list = this.list(new QueryWrapper<ClusterInfoEntity>().eq(Constants.CLUSTER_CODE, clusterInfo.getClusterCode()));
        if (Objects.nonNull(list) && list.size() >= 1) {
            return Result.error(Status.CLUSTER_CODE_EXISTS.getMsg());
        }
        clusterInfo.setCreateTime(new Date());
        clusterInfo.setCreateBy(SecurityUtils.getAuthUser().getUsername());
        clusterInfo.setClusterState(ClusterState.NEED_CONFIG);
        this.save(clusterInfo);
        //保存告警组与集群关系
        List<AlertGroupEntity> alertGroupList = alertGroupService.list();
        for (AlertGroupEntity alertGroupEntity : alertGroupList) {
            ClusterAlertGroupMap alertGroupMap = new ClusterAlertGroupMap();
            alertGroupMap.setAlertGroupId(alertGroupEntity.getId());
            alertGroupMap.setClusterId(clusterInfo.getId());
            groupMapService.save(alertGroupMap);
        }
        ProcessUtils.createServiceActor(clusterInfo);

        HashMap<String, String> globalVariables = new HashMap<>();
        globalVariables.put("${installPath}", Constants.INSTALL_PATH);
        globalVariables.put("${apiHost}", CacheUtils.getString("hostname"));
        globalVariables.put("${apiPort}", configBean.getServerPort());
        globalVariables.put("${HADOOP_HOME}", Constants.INSTALL_PATH + "/hadoop-3.3.3");

        CacheUtils.put("globalVariables" + Constants.UNDERLINE + clusterInfo.getId(), globalVariables);
        return Result.success();
    }


    @Override
    public Result getClusterList() {
        List<ClusterInfoEntity> list = this.list();
        for (ClusterInfoEntity clusterInfoEntity : list) {
            List<UserInfoEntity> userList = clusterUserService.getAllClusterManagerByClusterId(clusterInfoEntity.getId());
            clusterInfoEntity.setClusterManagerList(userList);
            clusterInfoEntity.setClusterStateCode(clusterInfoEntity.getClusterState().getValue());
        }
        return Result.success(list);
    }

    @Override
    public Result runningClusterList() {
        List<ClusterInfoEntity> list = this.list(new QueryWrapper<ClusterInfoEntity>().eq(Constants.CLUSTER_STATE, ClusterState.RUNNING));
        return Result.success(list);
    }

    @Override
    public Result updateClusterState(Integer clusterId, Integer clusterState) {
        ClusterInfoEntity clusterInfo = this.getById(clusterId);
        if (clusterState == 2) {
            clusterInfo.setClusterState(ClusterState.RUNNING);
        }
        this.updateById(clusterInfo);
        return Result.success();
    }

    @Override
    public List<ClusterInfoEntity> getClusterByFrameCode(String frameCode) {
        return this.list(new QueryWrapper<ClusterInfoEntity>().eq(Constants.CLUSTER_FRAME, frameCode));
    }

    @Override
    public Result updateCluster(ClusterInfoEntity clusterInfo) {
        //集群编码判重
        List<ClusterInfoEntity> list = this.list(new QueryWrapper<ClusterInfoEntity>().eq(Constants.CLUSTER_CODE, clusterInfo.getClusterCode()));
        if (Objects.nonNull(list) && list.size() >= 1) {
            ClusterInfoEntity clusterInfoEntity = list.get(0);
            if (clusterInfoEntity.getId() != clusterInfo.getId()) {
                return Result.error(Status.CLUSTER_CODE_EXISTS.getMsg());
            }
        }
        ClusterInfoEntity cluster = this.getById(clusterInfo.getId());
        if (cluster.getClusterCode() != clusterInfo.getClusterCode()) {
            ProcessUtils.createServiceActor(clusterInfo);
        }
        this.updateById(clusterInfo);
        return Result.success();
    }

    @Override
    public void deleteCluster(List<Integer> ids) {
        Integer id = ids.get(0);
        ClusterInfoEntity clusterInfo = this.getById(id);
        this.removeByIds(ids);
        List<FrameServiceEntity> frameServiceList = frameServiceService.getAllFrameServiceByFrameCode(clusterInfo.getClusterFrame());
        for (FrameServiceEntity frameServiceEntity : frameServiceList) {
            //创建服务actor
            ActorRef actor = ActorUtils.getLocalActor(ServiceActor.class,clusterInfo.getClusterCode() + "-serviceActor-" + frameServiceEntity.getServiceName());
            actor.tell(PoisonPill.getInstance(), ActorRef.noSender());
        }

    }
}
