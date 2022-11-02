package com.datasophon.api.service.strategy;

import com.datasophon.common.model.ServiceConfig;

import java.util.List;

public interface ServiceRoleStrategy {
    public void handler(Integer clusterId,List<String> hosts);

    void handlerConfig(Integer clusterId, List<ServiceConfig> list);
}
