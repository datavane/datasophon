package com.datasophon.api.strategy;

import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class FEHandlerStartegy implements ServiceRoleStrategy{

    private static final Logger logger = LoggerFactory.getLogger(FEHandlerStartegy.class);

    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+clusterId);
        if(hosts.size() >= 1){
            ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${feMaster}",hosts.get(0));
        }
    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {

    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }

    @Override
    public void handlerServiceRoleInfo(ServiceRoleInfo serviceRoleInfo, String hostname) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + serviceRoleInfo.getClusterId());
        if("SRFE".equals(serviceRoleInfo.getName()) || "DorisFE".equals(serviceRoleInfo.getName())){
            String feMaster = globalVariables.get("${feMaster}");
            if(hostname.equals(feMaster)){
                logger.info("fe master is {}",feMaster);
                serviceRoleInfo.setSortNum(1);
            }else{
                logger.info("set fe follower master");
                serviceRoleInfo.setMasterHost(feMaster);
                serviceRoleInfo.setSlave(true);
                serviceRoleInfo.setSortNum(2);
            }
        }
    }
}
