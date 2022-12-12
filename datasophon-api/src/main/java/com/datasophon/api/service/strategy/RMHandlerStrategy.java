package com.datasophon.api.service.strategy;

import com.alibaba.fastjson.JSONArray;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterYarnSchedulerService;
import com.datasophon.api.service.FrameServiceService;
import com.datasophon.api.utils.PackageUtils;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterYarnScheduler;
import com.datasophon.dao.entity.FrameServiceEntity;

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
        }
    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }

}
