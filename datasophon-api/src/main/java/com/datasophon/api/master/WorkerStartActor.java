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
import akka.pattern.Patterns;
import akka.util.Timeout;
import cn.hutool.core.util.ObjectUtil;
import com.datasophon.api.exceptions.ServiceException;
import com.datasophon.api.service.*;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.GenerateHostPrometheusConfig;
import com.datasophon.common.command.remote.CreateUnixGroupCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.model.StartWorkerMessage;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.dao.entity.*;
import com.datasophon.dao.enums.MANAGED;
import com.datasophon.dao.enums.ServiceRoleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WorkerStartActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(WorkerStartActor.class);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StartWorkerMessage) {
            StartWorkerMessage msg = (StartWorkerMessage) message;
            String hostname = msg.getHostname();
            Integer clusterId = msg.getClusterId();
            logger.info("receive message when worker first start :{}", hostname);

            ClusterHostService clusterHostService = SpringTool.getApplicationContext().getBean(ClusterHostService.class);
            ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);

            //is managed?
            ClusterHostEntity hostEntity = clusterHostService.getClusterHostByHostname(hostname);
            ClusterInfoEntity cluster = clusterInfoService.getById(clusterId);
            logger.info("host install set to 100%");
            if (CacheUtils.constainsKey(cluster.getClusterCode() + Constants.HOST_MAP)) {
                HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(cluster.getClusterCode() + Constants.HOST_MAP);
                HostInfo hostInfo = map.get(hostname);
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
                //sync cluster user and group
                syncClusterUserAndGroup(clusterId, hostname);
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
        }
    }

    private void syncClusterUserAndGroup(Integer clusterId, String hostname) {
        ClusterGroupService clusterGroupService = SpringTool.getApplicationContext().getBean(ClusterGroupService.class);
        ClusterUserService clusterUserService = SpringTool.getApplicationContext().getBean(ClusterUserService.class);

        List<ClusterGroup> userGroupList = clusterGroupService.listAllUserGroup(clusterId);
        for (ClusterGroup clusterGroup : userGroupList) {
            String groupName = clusterGroup.getGroupName();
            clusterGroupService.createUnixGroupOnHost(hostname, groupName);
        }
        List<ClusterUser> userList = clusterUserService.listAllUser(clusterId);
        for (ClusterUser clusterUser : userList) {
            clusterUserService.createUnixUserOnHost(clusterUser, hostname);
        }
    }

}
