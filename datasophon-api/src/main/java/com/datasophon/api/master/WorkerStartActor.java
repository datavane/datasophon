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
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.MANAGED;
import com.datasophon.dao.enums.ServiceRoleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
            ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().
                    getBean(ClusterServiceRoleInstanceService.class);
            ClusterServiceCommandService serviceCommandService = SpringTool.getApplicationContext().
                    getBean(ClusterServiceCommandService.class);
            //启动该节点上停止的服务
            List<ClusterServiceRoleInstanceEntity> list = roleInstanceService.getStoppedRoleInstanceOnHost(msg.getClusterId(),msg.getHostname(),ServiceRoleState.STOP);
            if (Objects.nonNull(list) && list.size() > 0) {
                Integer serviceId = list.get(0).getServiceId();
                List<String> idList = list.stream().map(e -> e.getId().toString()).collect(Collectors.toList());
                serviceCommandService.generateServiceRoleCommand(msg.getClusterId(), CommandType.START_SERVICE, serviceId, idList);
            }
            //判断当前主机是否已受管
            ClusterHostEntity hostEntity = clusterHostService.getClusterHostByHostname(msg.getHostname());
            ClusterInfoEntity cluster = clusterInfoService.getById(msg.getClusterId());
            //主机管理安装进度设为100%
            logger.info("host install set to 100%");
            if (CacheUtils.constainsKey(cluster.getClusterCode() + Constants.HOST_MAP)) {
                HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(cluster.getClusterCode() + Constants.HOST_MAP);
                HostInfo hostInfo = map.get(msg.getHostname());
                if (Objects.nonNull(hostInfo)) {
                    hostInfo.setProgress(100);
                    hostInfo.setInstallState(InstallState.SUCCESS);
                    hostInfo.setInstallStateCode(InstallState.SUCCESS.getValue());
                    hostInfo.setManaged(true);
                }
            }
            if (ObjectUtil.isNull(hostEntity)) {
                //主机信息持久化到数据库
                ProcessUtils.saveHostInstallInfo(msg, cluster.getClusterCode(), clusterHostService);
                logger.info("host install save to database");
            } else {
                hostEntity.setCpuArchitecture(msg.getCpuArchitecture());
                hostEntity.setManaged(MANAGED.YES);
                clusterHostService.updateById(hostEntity);
            }
            //添加主机监控到prometheus
            ActorRef prometheusActor = (ActorRef) CacheUtils.get("prometheusActor");
            GenerateHostPrometheusConfig prometheusConfigCommand = new GenerateHostPrometheusConfig();
            prometheusConfigCommand.setClusterId(cluster.getId());
            prometheusActor.tell(prometheusConfigCommand, getSelf());
        }
    }
}
