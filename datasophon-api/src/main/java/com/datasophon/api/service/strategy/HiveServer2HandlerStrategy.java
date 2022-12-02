package com.datasophon.api.service.strategy;

import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HiveServer2HandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(HiveServer2HandlerStrategy.class);
    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);

        if (hosts.size() > 1) {
            ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${masterHiveServer2}", hosts.get(0));
        }
    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {

    }
}
