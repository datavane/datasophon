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

package com.datasophon.api.master;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.hutool.core.util.ObjectUtil;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceCommandService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.GenerateHostPrometheusConfig;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.model.StartWorkerMessage;
import com.datasophon.common.utils.CollectionUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.MANAGED;
import com.datasophon.dao.enums.ServiceRoleState;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class WorkerStartActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(WorkerStartActor.class);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StartWorkerMessage) {
            StartWorkerMessage msg = (StartWorkerMessage) message;
            logger.info("receive message when worker first start :{}", msg.getHostname());

            ClusterHostService clusterHostService = SpringTool.getApplicationContext().getBean(ClusterHostService.class);
            ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);

            //is managed?
            ClusterHostEntity hostEntity = clusterHostService.getClusterHostByHostname(msg.getHostname());
            ClusterInfoEntity cluster = clusterInfoService.getById(msg.getClusterId());
            logger.info("host install set to 100%");
            if (CacheUtils.constainsKey(cluster.getClusterCode() + Constants.HOST_MAP)) {
                HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(cluster.getClusterCode() + Constants.HOST_MAP);
                HostInfo hostInfo = map.get(msg.getHostname());
                if (Objects.nonNull(hostInfo)) {
                    hostInfo.setProgress(Constants.ONE_HUNDRRD);
                    hostInfo.setInstallState(InstallState.SUCCESS);
                    hostInfo.setInstallStateCode(InstallState.SUCCESS.getValue());
                    hostInfo.setManaged(true);
                }
            }
            if (ObjectUtil.isNull(hostEntity)) {
                //save to db
                ProcessUtils.saveHostInstallInfo(msg, cluster.getClusterCode(), clusterHostService);
                logger.info("host install save to database");
            } else {
                hostEntity.setCpuArchitecture(msg.getCpuArchitecture());
                hostEntity.setManaged(MANAGED.YES);
                clusterHostService.updateById(hostEntity);
            }
            //add to prometheus
            ActorRef prometheusActor = ActorUtils.getLocalActor(PrometheusActor.class, ActorUtils.getActorRefName(PrometheusActor.class));
            GenerateHostPrometheusConfig prometheusConfigCommand = new GenerateHostPrometheusConfig();
            prometheusConfigCommand.setClusterId(cluster.getId());
            prometheusActor.tell(prometheusConfigCommand, getSelf());

            autoStartServiceNeeded(msg.getHostname(), cluster.getId());
        }
    }

    /**
     * Automatically start services that need to be started
     *
     * @param clusterId
     */
    private void autoStartServiceNeeded(String hostname, Integer clusterId) {
        ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);

        ClusterServiceRoleInstanceEntity prometheus = roleInstanceService
                .lambdaQuery()
                .eq(ClusterServiceRoleInstanceEntity::getClusterId, clusterId)
                .eq(ClusterServiceRoleInstanceEntity::getServiceName, "PROMETHEUS")
                .one();  // from v1.1.0, prometheus and alertmanager must install at the same node
        if (Objects.isNull(prometheus) || !Objects.equals(hostname, prometheus.getHostname())) {
            return;
        }

        List<ClusterServiceRoleInstanceEntity> stopedServiceList = roleInstanceService.getStoppedService(clusterId);
        if (CollectionUtils.isEmpty(stopedServiceList)) {
            logger.info("There's no service need to auto-start.");
            return;
        }

        Set<Integer> basicServiceIds = new HashSet<>(2);
        Set<Integer> otherServiceIds = new HashSet<>();
        stopedServiceList.forEach(i -> {
            String serviceName = i.getServiceName();
            Integer serviceId = i.getServiceId();
            if (Objects.equals(serviceName, "PROMETHEUS") || Objects.equals(serviceName, "ALERTMANAGER")) {
                basicServiceIds.add(serviceId);
            } else {
                otherServiceIds.add(serviceId);
            }
        });

        ClusterServiceCommandService serviceCommandService = SpringTool.getApplicationContext().getBean(ClusterServiceCommandService.class);
        Result result = null;
        if (CollectionUtils.isNotEmpty(basicServiceIds)) {
            result = startServices(serviceCommandService, clusterId, basicServiceIds);

            if (Objects.equals(result.getCode(), 200)) {
                logger.info("Auto-start service Prometheus and AlertManager successful.");
            } else {
                logger.info("Auto-start service Prometheus and AlertManager failed, terminate auto-start other services.\"");
                return;
            }
        }

        if (CollectionUtils.isEmpty(otherServiceIds)) {// no other service need to start
            return;
        }

        if (Objects.isNull(result) || Objects.equals(result.getCode(), 200)) { // prometheus and alertmanager is running
            logger.info("Service Prometheus and AlertManager is running，auto-start other services.");
            result = startServices(serviceCommandService, clusterId, otherServiceIds);

            if (Objects.equals(result.getCode(), 200)) {
                logger.info("Auto-start other services successful");
            } else {
                logger.info("Some service auto-start failed, please check logs of the services that failed to start.");
            }
        }
    }

    private Result startServices(ClusterServiceCommandService serviceCommandService,
                                 Integer clusterId,
                                 Set<Integer> serviceIdList) {
        String serviceIds = StringUtils.join(serviceIdList, ",");
        return serviceCommandService.generateServiceCommand(clusterId, CommandType.START_SERVICE.name(), serviceIds);
    }

}
