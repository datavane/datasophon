package com.datasophon.api.service.strategy;

import com.alibaba.fastjson.JSONArray;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.FrameServiceService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.FrameServiceEntity;

import java.util.List;
import java.util.Map;

public class NameNodeHandlerStrategy implements ServiceRoleStrategy{


    @Override
    public void handler(Integer clusterId,List<String> hosts) {
        FrameServiceService frameService = SpringTool.getApplicationContext().getBean(FrameServiceService.class);
        ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);
        ClusterInfoEntity clusterInfo = clusterInfoService.getById(clusterId);

        FrameServiceEntity frameServiceEntity = frameService.getServiceByFrameCodeAndServiceName(clusterInfo.getClusterFrame(), "HDFS");
        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+clusterId);
        if(hosts.size() == 1){
            ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${fs.defaultFS}",hosts.get(0)+":8020");
            updateServiceConfigToHA(frameService, frameServiceEntity, false, true);
        }else{
            //查询hdfs配置，更改高可用配置为require : true, hidden : false
            ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${nn1}",hosts.get(0));
            ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${nn2}",hosts.get(1));
            ProcessUtils.generateClusterVariable(globalVariables, clusterId,"${fs.defaultFS}","nameservice1");
            updateServiceConfigToHA(frameService, frameServiceEntity, true, false);
        }


    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
//        ServiceConfig serviceConfig = new ServiceConfig();
//
//        for (ServiceConfig config : list) {
//            if("enableRack".equals(config.getName())){
//                serviceConfig.setName("net.topology.script.file.name");
//                serviceConfig.setValue("");
//                serviceConfig.setRequired(true);
//                serviceConfig.setHidden(false);
//                serviceConfig.setType("input");
//            }
//        }
//        list.add(serviceConfig);
    }

    private void updateServiceConfigToHA(FrameServiceService frameService, FrameServiceEntity frameServiceEntity, boolean b, boolean b2) {
        String serviceConfig = frameServiceEntity.getServiceConfig();
        List<ServiceConfig> serviceConfigs = JSONArray.parseArray(serviceConfig, ServiceConfig.class);
        for (ServiceConfig config : serviceConfigs) {
            if ("ha".equals(config.getConfigType())) {
                config.setRequired(b);
                config.setHidden(b2);
            }
        }
        String newConfigs = JSONArray.toJSONString(serviceConfigs);
        frameServiceEntity.setServiceConfig(newConfigs);
        frameService.updateById(frameServiceEntity);
    }
}
