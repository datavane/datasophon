package com.datasophon.dao.model;

import lombok.Data;

import java.util.List;

@Data
public class ClusterQueueCapacityList {

    private String rootId;

    private List<ClusterQueueCapacity> nodes;

    private List<Links> links;
}
