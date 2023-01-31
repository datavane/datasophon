package com.datasophon.api.strategy;

import com.datasophon.api.load.ServiceConfigMap;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.common.utils.PlaceholderUtils;
import com.datasophon.dao.entity.ClusterInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HiveServer2HandlerStrategy implements ServiceRoleStrategy {
    private static final Logger logger = LoggerFactory.getLogger(HiveServer2HandlerStrategy.class);
    @Override
    public void handler(Integer clusterId, List<String> hosts) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        CacheUtils.put("enableHiveServer2HA",false);
        if (hosts.size() > 1) {
            CacheUtils.put("enableHiveServer2HA",true);
            ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${masterHiveServer2}", hosts.get(0));
        }
    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        boolean enableKerberos = false;
        Map<String, ServiceConfig> map = ProcessUtils.translateToMap(list);
        for (ServiceConfig config : list) {
            if("enableKerberos".equals(config.getName())){
                if( (Boolean)config.getValue()){
                    enableKerberos = true;
                    ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${enableHIVEKerberos}", "true");
                }else {
                    ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${enableHIVEKerberos}", "false");
                }
            }
        }
        String key = clusterInfo.getClusterFrame() + Constants.UNDERLINE + "HIVE" + Constants.CONFIG;
        List<ServiceConfig> configs = ServiceConfigMap.get(key);
        ArrayList<ServiceConfig> kbConfigs = new ArrayList<>();
        if(enableKerberos){
            for (ServiceConfig serviceConfig : configs) {
                if(serviceConfig.isConfigWithKerberos()){
                    if(map.containsKey(serviceConfig.getName())){
                        ServiceConfig config = map.get(serviceConfig.getName());
                        config.setRequired(true);
                        config.setHidden(false);
                        if(Constants.INPUT.equals(config.getType())) {
                            String value = PlaceholderUtils.replacePlaceholders((String) serviceConfig.getValue(), globalVariables, Constants.REGEX_VARIABLE);
                            config.setValue(value);
                        }
                    }else{
                        serviceConfig.setRequired(true);
                        serviceConfig.setHidden(false);
                        if(Constants.INPUT.equals(serviceConfig.getType())) {
                            String value = PlaceholderUtils.replacePlaceholders((String) serviceConfig.getValue(), globalVariables, Constants.REGEX_VARIABLE);
                            serviceConfig.setValue(value);
                        }
                        kbConfigs.add(serviceConfig);
                    }

                }
            }
        }else{
            for (ServiceConfig serviceConfig : configs) {
                if(serviceConfig.isConfigWithKerberos()){
                    if(map.containsKey(serviceConfig.getName())){
                        list.remove(map.get(serviceConfig.getName()));
                    }
                }
            }
        }
        list.addAll(kbConfigs);

    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {
        //if enabled hiveserver2 ha
        if((Boolean) CacheUtils.get("enableHiveServer2HA")){
            for (ServiceConfig serviceConfig : list) {
                if("ha".equals(serviceConfig.getConfigType())){
                    serviceConfig.setRequired(true);
                    serviceConfig.setHidden(false);
                }
            }
        }else{
            for (ServiceConfig serviceConfig : list) {
                if("ha".equals(serviceConfig.getConfigType())){
                    serviceConfig.setRequired(false);
                    serviceConfig.setHidden(true);
                }
            }
        }
    }

    @Override
    public void handlerServiceRoleInfo(ServiceRoleInfo serviceRoleInfo, String hostname) {
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + serviceRoleInfo.getClusterId());
        if(globalVariables.containsKey("${masterHiveServer2}") && !hostname.equals(globalVariables.get("${masterHiveServer2}"))){
            logger.info("set to slave hiveserver2");
            serviceRoleInfo.setSlave(true);
        }
    }
}
