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

import akka.actor.UntypedActor;
import cn.hutool.extra.spring.SpringUtil;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceInstanceService;
import com.datasophon.api.service.ClusterServiceRoleGroupConfigService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.service.host.ClusterHostService;
import com.datasophon.common.Constants;
import com.datasophon.common.command.ClusterCommand;
import com.datasophon.common.enums.ClusterCommandType;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterHostDO;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceInstanceEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.ClusterState;
import com.datasophon.dao.enums.ServiceRoleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 节点状态监测
 */
public class ClusterActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(ClusterActor.class);

    private static final String DEPRECATED = "Deprecated";

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof ClusterCommand) {
            logger.info("start to check cluster info");
            ClusterServiceRoleInstanceService roleInstanceService =
                    SpringUtil.getBean(ClusterServiceRoleInstanceService.class);
            ClusterInfoService clusterInfoService =
                    SpringUtil.getBean(ClusterInfoService.class);

            // Host or cluster
            final ClusterCommand clusterCommand = (ClusterCommand) msg;

            if (ClusterCommandType.CHECK.equals(clusterCommand.getCommandType())) {
                // 获取所有集群
                Result result = clusterInfoService.getClusterList();
                List<ClusterInfoEntity> clusterList = (List<ClusterInfoEntity>) result.getData();

                for (ClusterInfoEntity clusterInfoEntity : clusterList) {
                    // 获取集群上正在运行的服务
                    int clusterId = clusterInfoEntity.getId();
                    List<ClusterServiceRoleInstanceEntity> roleInstanceList = roleInstanceService.getServiceRoleInstanceListByClusterId(clusterId);
                    if (!ClusterState.NEED_CONFIG.equals(clusterInfoEntity.getClusterState())) {
                        if (!roleInstanceList.isEmpty()) {
                            if (roleInstanceList.stream().allMatch(roleInstance -> ServiceRoleState.STOP.equals(roleInstance.getServiceRoleState()))) {
                                clusterInfoService.updateClusterState(clusterId, ClusterState.STOP.getValue());
                            } else {
                                clusterInfoService.updateClusterState(clusterId, ClusterState.RUNNING.getValue());
                            }
                        }

                    }
                }

            } else if (ClusterCommandType.DELETE.equals(clusterCommand.getCommandType())) {
                Integer clusterId = clusterCommand.getClusterId();
                if (Objects.nonNull(clusterId)) {
                    ClusterInfoEntity clusterInfo = clusterInfoService.getById(clusterId);
                    if (Objects.nonNull(clusterInfo)) {
                        ClusterHostService clusterHostService = SpringUtil.getBean(ClusterHostService.class);
                        ClusterServiceInstanceService clusterServiceInstanceService = SpringUtil.getBean(ClusterServiceInstanceService.class);
                        ClusterServiceRoleInstanceService clusterServiceRoleInstanceService = SpringUtil.getBean(ClusterServiceRoleInstanceService.class);
                        ClusterServiceRoleGroupConfigService clusterServiceRoleGroupConfigService = SpringUtil.getBean(ClusterServiceRoleGroupConfigService.class);

                        // 检查服务实例配置与目录
//                        List<ClusterServiceRoleInstanceEntity> roleInstanceList = clusterServiceRoleInstanceService.getServiceRoleInstanceListByClusterId(clusterId);
//                        for (ClusterServiceRoleInstanceEntity roleInstance : roleInstanceList) {
//                            String roleName = roleInstance.getServiceRoleName();
//                            String hostname = roleInstance.getHostname();
//                            ClusterServiceRoleGroupConfig config = clusterServiceRoleGroupConfigService.getConfigByRoleGroupId(roleInstance.getRoleGroupId());
//                            Map<Generators, List<ServiceConfig>> configFileMap = new ConcurrentHashMap<>();
//                            ProcessUtils.generateConfigFileMap(configFileMap, config);
//                            Predicate<ServiceConfig> filter = c -> Constants.PATH.equals(c.getConfigType()) && !((String) c.getValue()).contains(DEPRECATED);
//                            for (Map.Entry<Generators, List<ServiceConfig>> configFile : configFileMap.entrySet()) {
//                                List<ServiceConfig> serviceConfigs = configFile.getValue().stream()
//                                        .filter(filter)
//                                        .peek(c -> {
//                                            String oldPath = (String) c.getValue();
//                                            String newPath = String.format("%s_%s_%s_%s", oldPath, DEPRECATED, clusterId, DateUtil.today());
//                                            c.setValue(newPath);
//                                            c.setConfigType(Constants.MV_PATH);
//                                        })
//                                        .collect(Collectors.toList());
//                                if (!serviceConfigs.isEmpty()) {
//                                    configFileMap.replace(configFile.getKey(), serviceConfigs);
//                                } else {
//                                    configFileMap.remove(configFile.getKey());
//                                }
//                            }
//
//                            if (!configFileMap.isEmpty()) {
//                                // 分发重命名命令
//                                ExecResult execResult = new ExecResult();
//                                try {
//                                    logger.info(
//                                            "start to uninstall {} in host {}",
//                                            roleName,
//                                            hostname);
//                                    execResult = ProcessUtils.configServiceRoleInstance(clusterInfo, configFileMap, roleInstance);
//                                    if (Objects.nonNull(execResult) && execResult.getExecResult()) {
//                                        logger.info(
//                                                "{} uninstall success in {}",
//                                                roleName,
//                                                hostname);
//                                    } else {
//                                        logger.info(
//                                                "{} uninstall failed in {}",
//                                                roleName,
//                                                hostname);
//                                        return;
//                                    }
//
//                                } catch (Exception e) {
//                                    logger.info(
//                                            "{} uninstall failed in {}",
//                                            roleName,
//                                            hostname);
//                                    logger.error(ProcessUtils.getExceptionMessage(e));
//                                    return;
//                                }
//                            }
//                        }
                        List<ClusterServiceInstanceEntity> serviceInstanceList = clusterServiceInstanceService.listAll(clusterId);
                        if(serviceInstanceList.stream().allMatch(instance ->  clusterServiceInstanceService.delServiceInstance(instance.getId()).isSuccess())) {
                            List<ClusterHostDO> hostList = clusterHostService.getHostListByClusterId(clusterId);
                            clusterHostService.deleteHosts(hostList.stream().map(h -> String.valueOf(h.getId())).collect(Collectors.joining(Constants.COMMA)));
                            clusterInfoService.removeById(clusterId);
                        }
                    }
                }
            }
        } else {
            unhandled(msg);
        }
    }
}
