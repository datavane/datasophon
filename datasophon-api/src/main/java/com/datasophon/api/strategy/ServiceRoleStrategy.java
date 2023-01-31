package com.datasophon.api.strategy;

import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleInfo;

import java.util.List;

public interface ServiceRoleStrategy {
    public void handler(Integer clusterId,List<String> hosts);

    void handlerConfig(Integer clusterId, List<ServiceConfig> list);

    void getConfig(Integer clusterId, List<ServiceConfig> list);

    void handlerServiceRoleInfo(ServiceRoleInfo serviceRoleInfo,String hostname);
}
