package com.datasophon.worker.strategy;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRoleStrategyContext {
    private static final Map<String, ServiceRoleStrategy> map = new ConcurrentHashMap<>();

    static {
        map.put("NameNode",new NameNodeHandlerStrategy());
        map.put("ZKFC",new ZKFCHandlerStrategy());
        map.put("JournalNode",new JournalNodeHandlerStrategy());
        map.put("DataNode",new DataNodeHandlerStrategy());
        map.put("ResourceManager",new ResourceManagerHandlerStrategy());
        map.put("NodeManager",new NodeManagerHandlerStrategy());
        map.put("RangerAdmin",new RangerAdminHandlerStrategy());
        map.put("HiveServer2",new HiveServer2HandlerStrategy());
        map.put("HbaseMaster",new HbaseHandlerStrategy());
        map.put("RegionServer",new HbaseHandlerStrategy());
        map.put("Krb5Kdc",new KerberosHandlerStrategy());
        map.put("FE",new FEHandlerStrategy());
        map.put("ZkServer",new ZkServerHandlerStrategy());
    }

    public static ServiceRoleStrategy getServiceRoleHandler(String type){
        if(StringUtils.isBlank(type)){
            return null;
        }
        return map.get(type);
    }
}
