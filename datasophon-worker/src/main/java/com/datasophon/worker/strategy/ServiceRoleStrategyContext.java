/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.datasophon.worker.strategy;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRoleStrategyContext {

    private static final Map<String, ServiceRoleStrategy> map = new ConcurrentHashMap<>();

    static {
        map.put("NameNode", new NameNodeHandlerStrategy());
        map.put("ZKFC", new ZKFCHandlerStrategy());
        map.put("JournalNode", new JournalNodeHandlerStrategy());
        map.put("DataNode", new DataNodeHandlerStrategy());
        map.put("ResourceManager", new ResourceManagerHandlerStrategy());
        map.put("NodeManager", new NodeManagerHandlerStrategy());
        map.put("RangerAdmin", new RangerAdminHandlerStrategy());
        map.put("HiveServer2", new HiveServer2HandlerStrategy());
        map.put("HbaseMaster", new HbaseHandlerStrategy());
        map.put("RegionServer", new HbaseHandlerStrategy());
        map.put("Krb5Kdc", new Krb5KdcHandlerStrategy());
        map.put("KAdmin", new KAdminHandlerStrategy());
        map.put("SRFE", new FEHandlerStrategy());
        map.put("DorisFE", new FEHandlerStrategy());
        map.put("ZkServer", new ZkServerHandlerStrategy());
        map.put("KafkaBroker", new KafkaHandlerStrategy());
        map.put("SRBE", new BEHandlerStrategy());
        map.put("DorisBE", new BEHandlerStrategy());
        map.put("HistoryServer", new HistoryServerHandlerStrategy());
    }

    public static ServiceRoleStrategy getServiceRoleHandler(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        return map.get(type);
    }
}
