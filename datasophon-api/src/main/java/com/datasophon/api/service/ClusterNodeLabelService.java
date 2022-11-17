package com.datasophon.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterNodeLabelEntity;

public interface ClusterNodeLabelService extends IService<ClusterNodeLabelEntity> {
    Result saveNodeLabel(Integer clusterId, String nodeLabel);

    Result deleteNodeLabel(Integer nodeLabelId);

    Result assignNodeLabel(Integer nodeLabelId, String hostIds);
}
