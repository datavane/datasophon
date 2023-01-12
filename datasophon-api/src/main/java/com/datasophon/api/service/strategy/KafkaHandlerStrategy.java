package com.datasophon.api.service.strategy;

import com.datasophon.api.load.ServiceConfigMap;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.dao.entity.ClusterInfoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KafkaHandlerStrategy extends ServiceHandlerAbstract implements ServiceRoleStrategy {
    @Override
    public void handler(Integer clusterId, List<String> hosts) {

    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        boolean enableKerberos = false;
        Map<String, ServiceConfig> map = ProcessUtils.translateToMap(list);
        for (ServiceConfig config : list) {
            if("enableKerberos".equals(config.getName())){
                enableKerberos = isEnableKerberos(clusterId, globalVariables, enableKerberos, config,"KAFKA");
            }
        }
        String key = clusterInfo.getClusterFrame() + Constants.UNDERLINE + "KAFKA" + Constants.CONFIG;
        List<ServiceConfig> configs = ServiceConfigMap.get(key);
        ArrayList<ServiceConfig> kbConfigs = new ArrayList<>();
        if(enableKerberos){
            addConfigWithKerberos(globalVariables, map, configs, kbConfigs);
        }else{
            removeConfigWithKerberos(list, map, configs);
        }
        list.addAll(kbConfigs);
    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }
}
