package com.datasophon.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.service.ClusterNodeLabelService;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterNodeLabelEntity;
import com.datasophon.dao.mapper.ClusterNodeLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service("clusterNodeLabelService")
@Transactional
public class ClusterNodeLabelServiceImpl extends ServiceImpl<ClusterNodeLabelMapper, ClusterNodeLabelEntity>  implements ClusterNodeLabelService {

    @Autowired
    private ClusterHostService hostService;

    @Override
    public Result saveNodeLabel(Integer clusterId, String nodeLabel) {
        if(repeatNodeLable(clusterId,nodeLabel)){
            return Result.error("repeat node label");
        }
        ClusterNodeLabelEntity nodeLabelEntity = new ClusterNodeLabelEntity();
        nodeLabelEntity.setClusterId(clusterId);
        nodeLabelEntity.setNodeLabel(nodeLabel);
        this.save(nodeLabelEntity);
        return Result.success();
    }

    @Override
    public Result deleteNodeLabel(Integer nodeLabelId) {
        ClusterNodeLabelEntity nodeLabelEntity = this.getById(nodeLabelId);

        if(nodeLabelInUse(nodeLabelEntity.getNodeLabel())){
            return Result.error("node label is using");
        }
        this.removeById(nodeLabelId);
        return Result.success();
    }

    @Override
    public Result assignNodeLabel(Integer nodeLabelId, String hostIds) {
        ClusterNodeLabelEntity nodeLabelEntity = this.getById(nodeLabelId);
        hostService.updateBatchNodeLabel(hostIds,nodeLabelEntity.getNodeLabel());
        //sync to yarn

        return Result.success();
    }

    private boolean nodeLabelInUse(String nodeLabel) {
        List<ClusterHostEntity> list = hostService.list(new QueryWrapper<ClusterHostEntity>()
                .eq(Constants.NODE_LABEL, nodeLabel));
        if(list.size() > 0){
            return true;
        }
        return false;
    }

    private boolean repeatNodeLable(Integer clusterId, String nodeLabel) {
        List<ClusterNodeLabelEntity> list = this.list(new QueryWrapper<ClusterNodeLabelEntity>()
                .eq(Constants.CLUSTER_ID, clusterId)
                .eq(Constants.NODE_LABEL,nodeLabel));
        if(list.size() > 0){
            return true;
        }
        return false;
    }
}
