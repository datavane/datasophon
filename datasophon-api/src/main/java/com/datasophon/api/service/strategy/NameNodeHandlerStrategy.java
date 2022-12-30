package com.datasophon.api.service.strategy;

import com.alibaba.fastjson.JSONArray;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.FrameServiceService;
import com.datasophon.api.utils.PackageUtils;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.FrameServiceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NameNodeHandlerStrategy implements ServiceRoleStrategy {


    @Override
    public void handler(Integer clusterId, List<String> hosts) {

        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + clusterId);

        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${nn1}", hosts.get(0));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${nn2}", hosts.get(1));
        ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${fs.defaultFS}", "nameservice1");


    }

    @Override
    public void handlerConfig(Integer clusterId, List<ServiceConfig> list) {
        ClusterInfoEntity clusterInfo = ProcessUtils.getClusterInfo(clusterId);
        List<ServiceConfig> serviceConfigs = new ArrayList<>();
        boolean enableRack = false;
        Map<String, ServiceConfig> map = translateToMap(list);

        for (ServiceConfig config : list) {
            if ("enableRack".equals(config.getName())) {
                if( (Boolean)config.getValue()){
                    ServiceConfig serviceConfig = ProcessUtils.createServiceConfig("net.topology.table.file.name",Constants.INSTALL_PATH +
                            Constants.SLASH +
                            PackageUtils.getServiceDcPackageName(clusterInfo.getClusterFrame(), "HDFS")+
                            "/etc/hadoop/rack.properties","input");
                    ServiceConfig mapImplConfig = ProcessUtils.createServiceConfig("net.topology.node.switch.mapping.impl", "org.apache.hadoop.net.TableMapping","input");
                    serviceConfigs.add(serviceConfig);
                    serviceConfigs.add(mapImplConfig);
                    enableRack = true;
                }
            }
        }
        list.addAll(serviceConfigs);
        if(!enableRack){
            if(map.containsKey("net.topology.table.file.name")){
                list.remove(map.get("net.topology.table.file.name"));
            }
            if(map.containsKey("net.topology.node.switch.mapping.impl")){
                list.remove(map.get("net.topology.node.switch.mapping.impl"));
            }
        }
    }

    private Map<String, ServiceConfig> translateToMap(List<ServiceConfig> list) {
        return  list.stream().collect(Collectors.toMap(ServiceConfig::getName, serviceConfig -> serviceConfig, (v1, v2) -> v1));
    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }


}
