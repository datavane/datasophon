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
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.command.ClusterCheckCommand;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.ClusterState;
import com.datasophon.dao.enums.ServiceRoleState;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 节点状态监测
 */
public class ClusterCheckActor extends UntypedActor {

  private static final Logger logger = LoggerFactory.getLogger(ClusterCheckActor.class);

  @Override
  public void onReceive(Object msg) throws Throwable {
    if (msg instanceof ClusterCheckCommand) {
      logger.info("start to check cluster info");
      ClusterServiceRoleInstanceService roleInstanceService =
          SpringUtil.getBean(ClusterServiceRoleInstanceService.class);
      ClusterInfoService clusterInfoService =
          SpringUtil.getBean(ClusterInfoService.class);

      // Host or cluster
      final ClusterCheckCommand hostCheckCommand = (ClusterCheckCommand) msg;

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
    } else {
      unhandled(msg);
    }
  }
}
