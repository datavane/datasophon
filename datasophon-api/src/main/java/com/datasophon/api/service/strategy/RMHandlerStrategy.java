package com.datasophon.api.service.strategy;

import com.datasophon.api.load.ServiceConfigMap;
import com.datasophon.api.service.ClusterYarnSchedulerService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.utils.PlaceholderUtils;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterYarnScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RMHandlerStrategy implements ServiceRoleStrategy{
    @Override
    public void handler(Integer clusterId,List<String> hosts) {

        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+clusterId);

        ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${rm1}",hosts.get(0));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${rm2}",hosts.get(1));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${rmHost}",String.join(",",hosts));

    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        ClusterYarnSchedulerService schedulerService = SpringTool.getApplicationContext().getBean(ClusterYarnSchedulerService.class);
        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        boolean enableKerberos = false;
        Map<String, ServiceConfig> map = ProcessUtils.translateToMap(list);
        for (ServiceConfig config : list) {
            if ("yarn.resourcemanager.scheduler.class".equals(config.getName())) {
                ClusterYarnScheduler scheduler = schedulerService.getScheduler(clusterId);
                if("org.apache.hadoop.yarn.server.resourcemanager.scheduler.fair.FairScheduler".equals(config.getValue())){
                    if("capacity".equals(scheduler.getScheduler())){
                        scheduler.setScheduler("fair");
                        schedulerService.updateById(scheduler);
                    }
                }else {
                    if("fair".equals(scheduler.getScheduler())){
                        scheduler.setScheduler("capacity");
                        schedulerService.updateById(scheduler);
                    }
                }
            }
            if("enableKerberos".equals(config.getName())){
                if( (Boolean)config.getValue()){
                    enableKerberos = true;
                    ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${enableYARNKerberos}", "true");
                }else {
                    ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${enableYARNKerberos}", "false");
                }
            }
        }
        String key = clusterInfo.getClusterFrame() + Constants.UNDERLINE + "YARN" + Constants.CONFIG;
        List<ServiceConfig> configs = ServiceConfigMap.get(key);
        ArrayList<ServiceConfig> kbConfigs = new ArrayList<>();
        if(enableKerberos){
            for (ServiceConfig serviceConfig : configs) {
                if("kb".equals(serviceConfig.getConfigType())){
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
                if("kb".equals(serviceConfig.getConfigType())){
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

    }

}
