package com.datasophon.api.service.strategy;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRoleStrategyContext {
    private static final Map<String,ServiceRoleStrategy> map = new ConcurrentHashMap<>();

    static {
        map.put("NameNode",new NameNodeHandlerStrategy());
        map.put("ResourceManager",new RMHandlerStrategy());
        map.put("HiveMetaStore",new HiveMetaStroreHandlerStrategy());
        map.put("Grafana",new GrafanaHandlerStrategy());
        map.put("ZkServer",new ZkServerHandlerStrategy());
        map.put("HistoryServer",new HistoryServerHandlerStrategy());
        map.put("TrinoCoordinator",new TrinoHandlerStrategy());
        map.put("JournalNode",new JournalNodeHandlerStrategy());
        map.put("ZKFC",new ZKFCHandlerStrategy());
        map.put("FE",new StarRocksHandlerStartegy());
        map.put("RangerAdmin",new RangerAdminHandlerStrategy());
        map.put("ElasticSearch",new ElasticSearchHandlerStrategy());
        map.put("RANGER",new RangerAdminHandlerStrategy());
        map.put("ZOOKEEPER",new ZkServerHandlerStrategy());
        map.put("YARN",new RMHandlerStrategy());
        map.put("HDFS",new NameNodeHandlerStrategy());
    }

    public static ServiceRoleStrategy getServiceRoleHandler(String type){
        if(StringUtils.isBlank(type)){
            return null;
        }
        return map.get(type);
    }
}
