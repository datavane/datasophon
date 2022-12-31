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

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        for (ServiceConfig config : list) {
            if ("enableRack".equals(config.getName()) && (Boolean)config.getValue()) {
                ServiceConfig serviceConfig = ProcessUtils.createServiceConfig("net.topology.table.file.name",Constants.INSTALL_PATH +
                        Constants.SLASH +
                        PackageUtils.getServiceDcPackageName(clusterInfo.getClusterFrame(), "HDFS")+
                        "/etc/hadoop/rack.properties","input");
                ServiceConfig mapImplConfig = ProcessUtils.createServiceConfig("net.topology.node.switch.mapping.impl", "org.apache.hadoop.net.TableMapping","input");
                list.add(serviceConfig);
                list.add(mapImplConfig);
            }
        }

    }

    @Override
    public void getConfig(Integer clusterId, List<ServiceConfig> list) {

    }


}
