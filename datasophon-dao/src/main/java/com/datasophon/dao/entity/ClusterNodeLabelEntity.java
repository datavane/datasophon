package com.datasophon.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_ddh_cluster_node_label")
public class ClusterNodeLabelEntity {

    private Integer clusterId;

    private String nodeLabel;
}
