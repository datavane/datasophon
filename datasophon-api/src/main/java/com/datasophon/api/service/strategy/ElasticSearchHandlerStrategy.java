package com.datasophon.api.service.strategy;

import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;

import java.util.List;
import java.util.Map;

public class ElasticSearchHandlerStrategy implements ServiceRoleStrategy {
    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+clusterId);

        ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${initMasterNodes}",String.join(",",hosts));
        String join = String.join(":9300,", hosts);
        String seedHosts = join + ":9300";
        ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${seedHosts}",seedHosts);

    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {

    }
}
