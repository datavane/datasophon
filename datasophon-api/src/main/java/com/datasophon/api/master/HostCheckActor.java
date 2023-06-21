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
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.command.HostCheckCommand;
import com.datasophon.common.command.PingCommand;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.PromInfoUtils;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.MANAGED;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 节点状态监测
 */
public class HostCheckActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(HostCheckActor.class);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof HostCheckCommand) {
            logger.info("start to check host info");
            ClusterHostService clusterHostService =
                    SpringTool.getApplicationContext().getBean(ClusterHostService.class);
            ClusterServiceRoleInstanceService roleInstanceService =
                    SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
            ClusterInfoService clusterInfoService =
                    SpringTool.getApplicationContext().getBean(ClusterInfoService.class);

            // Host or cluster
            final HostCheckCommand hostCheckCommand = (HostCheckCommand)msg;
            final HostInfo hostInfo = hostCheckCommand.getHostInfo();

            // 获取当前安装并且正在运行的集群
            Result result = clusterInfoService.runningClusterList();
            List<ClusterInfoEntity> clusterList = (List<ClusterInfoEntity>) result.getData();

            for (ClusterInfoEntity clusterInfoEntity : clusterList) {
                // 获取集群上安装的 Prometheus 服务, 从 Prometheus 获取CPU、磁盘使用量等
                ClusterServiceRoleInstanceEntity prometheusInstance =
                        roleInstanceService.getOneServiceRole("Prometheus", "", clusterInfoEntity.getId());
                if (Objects.nonNull(prometheusInstance)) {
                    // 集群正常安装了 Prometheus
                    List<ClusterHostEntity> list = clusterHostService.getHostListByClusterId(clusterInfoEntity.getId());
                    String promUrl = "http://" + prometheusInstance.getHostname() + ":9090/api/v1/query";
                    for (ClusterHostEntity clusterHostEntity : list) {
                        if(hostInfo != null && !StringUtils.equals(clusterHostEntity.getHostname(), hostInfo.getHostname())) {
                            // 指定了节点，直接只处理这一个节点的
                            continue;
                        }
                        try {
                            // rpc 检测
                            final ActorRef pingActor = ActorUtils.getRemoteActor(clusterHostEntity.getHostname(), "pingActor");
                            PingCommand pingCommand = new PingCommand();
                            pingCommand.setMessage("ping");
                            Timeout timeout = new Timeout(Duration.create(180, TimeUnit.SECONDS));
                            Future<Object> execFuture = Patterns.ask(pingActor, pingCommand, timeout);
                            ExecResult execResult = (ExecResult) Await.result(execFuture, timeout.duration());
                            if (execResult.getExecResult()) {
                                logger.info("ping host: {} success", clusterHostEntity.getHostname());
                            } else {
                                logger.warn("ping host: {} fail, reason: {}", clusterHostEntity.getHostname(), execResult.getExecOut());
                                throw new IllegalStateException("ping host: " + clusterHostEntity.getHostname() + " failed.");
                            }
                            clusterHostEntity.setHostState(1);
                            clusterHostEntity.setManaged(MANAGED.YES);
                        } catch (Exception e) {
                            logger.warn("host: " + clusterHostEntity.getHostname() + " rpc error, cause: " + e.getMessage());
                            clusterHostEntity.setHostState(3);
                            clusterHostEntity.setManaged(MANAGED.NO);
                            // ping 失败，则修改节点状态为 false
                            continue;
                        }
                        try {
                            String hostname = clusterHostEntity.getHostname();
                            // 查询内存总量
                            String totalMemPromQl = "node_memory_MemTotal_bytes{job=~\"node\",instance=\"" + hostname
                                    + ":9100\"}/1024/1024/1024";
                            String totalMemStr = PromInfoUtils.getSinglePrometheusMetric(promUrl, totalMemPromQl);
                            if (StringUtils.isNotBlank(totalMemStr)) {
                                int totalMem = Double.valueOf(totalMemStr).intValue();
                                clusterHostEntity.setTotalMem(totalMem);
                            }
                            // 查询内存使用量
                            String memAvailablePromQl = "node_memory_MemAvailable_bytes{job=~\"node\",instance=\""
                                    + hostname + ":9100\"}/1024/1024/1024";
                            String memAvailableStr =
                                    PromInfoUtils.getSinglePrometheusMetric(promUrl, memAvailablePromQl);
                            if (StringUtils.isNotBlank(memAvailableStr)) {
                                int memAvailable = Double.valueOf(memAvailableStr).intValue();
                                Integer memUsed = clusterHostEntity.getTotalMem() - memAvailable;
                                clusterHostEntity.setUsedMem(memUsed);
                            }
                            // 总磁盘容量
                            String totalDistPromQl = "sum(node_filesystem_size_bytes{instance=\"" + hostname
                                    + ":9100\",fstype=~\"ext4|xfs\",mountpoint !~\".*pod.*\"})/1024/1024/1024";
                            String totalDiskStr = PromInfoUtils.getSinglePrometheusMetric(promUrl, totalDistPromQl);
                            if (StringUtils.isNotBlank(totalDiskStr)) {
                                int totalDisk = Double.valueOf(totalDiskStr).intValue();
                                clusterHostEntity.setTotalDisk(totalDisk);
                            }
                            // 查询磁盘使用量
                            String diskUsedPromQl = "sum(node_filesystem_size_bytes{instance=\"" + hostname
                                    + ":9100\",fstype=~\"ext.*|xfs\",mountpoint !~\".*pod.*\"}-node_filesystem_free_bytes{instance=\""
                                    + hostname
                                    + ":9100\",fstype=~\"ext.*|xfs\",mountpoint !~\".*pod.*\"})/1024/1024/1024";
                            String diskUsed = PromInfoUtils.getSinglePrometheusMetric(promUrl, diskUsedPromQl);
                            if (StringUtils.isNotBlank(diskUsed)) {
                                clusterHostEntity.setUsedDisk(Double.valueOf(diskUsed).intValue());
                            }
                            // 查询cpu负载
                            String cpuLoadPromQl = "node_load5{job=~\"node\",instance=\"" + hostname + ":9100\"}";
                            String cpuLoad = PromInfoUtils.getSinglePrometheusMetric(promUrl, cpuLoadPromQl);
                            if (StringUtils.isNotBlank(cpuLoad)) {
                                clusterHostEntity.setAverageLoad(cpuLoad);
                            }
                        } catch (Exception e) {
                            logger.warn("check cluster state error, cause: {}", e.getMessage());
                        }
                    }
                    if (list.size() > 0) {
                        clusterHostService.updateBatchById(list);
                    }
                } else {
                    // 没有 Prometheus？直接获取节点，通过 rpc 检测是否启动
                    List<ClusterHostEntity> hosts = clusterHostService.getHostListByClusterId(clusterInfoEntity.getId());
                    List<ClusterHostEntity> checkedHosts = new ArrayList<>(hosts.size());
                    for (ClusterHostEntity host : hosts) {
                        if(hostInfo != null && !StringUtils.equals(host.getHostname(), hostInfo.getHostname())) {
                            // 指定了节点，直接只处理这一个节点的
                            continue;
                        }
                        // copy 一个新的，只更新状态
                        ClusterHostEntity checkedHost = new ClusterHostEntity();
                        checkedHost.setId(host.getId());
                        checkedHost.setCheckTime(new Date());
                        try {
                            // rpc 检测
                            final ActorRef pingActor = ActorUtils.getRemoteActor(host.getHostname(), "pingActor");
                            PingCommand pingCommand = new PingCommand();
                            pingCommand.setMessage("ping");
                            Timeout timeout = new Timeout(Duration.create(180, TimeUnit.SECONDS));
                            Future<Object> execFuture = Patterns.ask(pingActor, pingCommand, timeout);
                            ExecResult execResult = (ExecResult) Await.result(execFuture, timeout.duration());
                            if (execResult.getExecResult()) {
                                logger.info("ping host: {} success", host.getHostname());
                            } else {
                                logger.warn("ping host: {} fail, reason: {}", host.getHostname(), execResult.getExecOut());
                                throw new IllegalStateException("ping host: " + host.getHostname() + " failed.");
                            }
                            checkedHost.setHostState(1);
                            checkedHost.setManaged(MANAGED.YES);
                        } catch (Exception e) {
                            logger.warn("host: " + host.getHostname() + " rpc error, cause: " + e.getMessage());
//                            checkedHost.setManaged(MANAGED.NO);
                            checkedHost.setHostState(2);
                        }
                        checkedHosts.add(checkedHost);
                    }
                    if (checkedHosts.size() > 0) {
                        clusterHostService.updateBatchById(checkedHosts);
                    }
                }
            }
        } else {
            unhandled(msg);
        }
    }
}
