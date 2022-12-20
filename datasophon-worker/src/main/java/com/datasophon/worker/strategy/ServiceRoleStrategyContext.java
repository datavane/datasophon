package com.datasophon.worker.strategy;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRoleStrategyContext {
    private static final Map<String, ServiceRoleStrategy> MAP = new ConcurrentHashMap<>();

    static {
        MAP.put("NameNode",new NameNodeHandlerStrategy());
        MAP.put("ZKFC",new ZKFCHandlerStrategy());
        MAP.put("RangerAdmin",new RangerAdminHandlerStrategy());
        MAP.put("HiveServer2",new HiveServer2HandlerStrategy());
        MAP.put("HbaseMaster",new HbaseHandlerStrategy());
        MAP.put("RegionServer",new HbaseHandlerStrategy());
        MAP.put("FE",new FEHandlerStrategy());
    }

    public static ServiceRoleStrategy getServiceRoleHandler(String type){
        if(StringUtils.isBlank(type)){
            return null;
        }
        return map.get(type);
    }
}
