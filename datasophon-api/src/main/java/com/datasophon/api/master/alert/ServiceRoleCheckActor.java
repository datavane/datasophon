package com.datasophon.api.master.alert;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.load.ServiceInfoMap;
import com.datasophon.api.load.ServiceRoleMap;
import com.datasophon.api.master.ActorUtils;
import com.datasophon.api.service.ClusterAlertHistoryService;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceInstanceService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.Constants;
import com.datasophon.common.command.ExecuteCmdCommand;
import com.datasophon.common.command.ServiceRoleCheckCommand;
import com.datasophon.common.model.ServiceInfo;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.dao.entity.ClusterAlertHistory;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.dao.entity.ClusterServiceInstanceEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.AlertLevel;
import com.datasophon.dao.enums.ServiceRoleState;
import com.datasophon.dao.enums.ServiceState;
import org.apache.commons.lang.StringUtils;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ServiceRoleCheckActor extends UntypedActor {

    private static final String promQl = "up{job=\"{}\",instance=\"{}.{}\"}";


    /**
     * 1、查询所有服务角色
     * 2、遍历服务角色，查询对应prometheus上服务角色状态
     * 3、服务状态若有变更，则更新到数据库
     *
     * @param msg
     * @throws Throwable
     */
    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof ServiceRoleCheckCommand) {//定时检测prometheus和alertmanager
            ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
            ClusterInfoService clusterInfoService = SpringTool.getApplicationContext().getBean(ClusterInfoService.class);

            List<ClusterServiceRoleInstanceEntity> list = roleInstanceService.list(new QueryWrapper<ClusterServiceRoleInstanceEntity>()
                    .in(Constants.SERVICE_ROLE_NAME, "Prometheus", "AlertManager", "Krb5Kdc", "KAdmin"));
            String frameCode = "";
            if (Objects.nonNull(list) && list.size() > 0) {
                for (ClusterServiceRoleInstanceEntity roleInstanceEntity : list) {
                    if ("Prometheus".equals(roleInstanceEntity.getServiceRoleName())) {
                        String url = "http://" + roleInstanceEntity.getHostname() + ":9090";
                        try {
                            HttpUtil.get(url);
                            //恢复告警
                            recoverAlert(roleInstanceEntity, roleInstanceService);
                        } catch (Exception e) {
                            saveAlert(roleInstanceEntity, roleInstanceService);
                        }
                    }
                    if ("AlertManager".equals(roleInstanceEntity.getServiceRoleName())) {
                        String url = "http://" + roleInstanceEntity.getHostname() + ":9093";
                        try {
                            HttpUtil.get(url);
                            recoverAlert(roleInstanceEntity, roleInstanceService);
                        } catch (Exception e) {
                            //产生告警
                            saveAlert(roleInstanceEntity, roleInstanceService);

                        }
                    }
                    if ("Krb5Kdc".equals(roleInstanceEntity.getServiceRoleName()) ||
                            "KAdmin".equals(roleInstanceEntity.getServiceRoleName())) {
                        Integer clusterId = roleInstanceEntity.getClusterId();
                        if (StringUtils.isBlank(frameCode)) {
                            ClusterInfoEntity cluster = clusterInfoService.getById(clusterId);
                            frameCode = cluster.getClusterFrame();
                        }
                        String key = frameCode + Constants.UNDERLINE + roleInstanceEntity.getServiceName() + Constants.UNDERLINE + roleInstanceEntity.getServiceRoleName();
                        ServiceRoleInfo serviceRoleInfo = ServiceRoleMap.get(key);
                        ServiceInfo serviceInfo = ServiceInfoMap.get(frameCode + Constants.UNDERLINE + roleInstanceEntity.getServiceName());

                        ActorSelection execCmdActor = ActorUtils.actorSystem.actorSelection("akka.tcp://datasophon@" + roleInstanceEntity.getHostname() + ":2552/user/worker/executeCmdActor");
                        ExecuteCmdCommand cmdCommand = new ExecuteCmdCommand();
                        ArrayList<String> commandList = new ArrayList<>();
                        commandList.add(serviceInfo.getDecompressPackageName() + Constants.SLASH + serviceRoleInfo.getStatusRunner().getProgram());
                        commandList.addAll(serviceRoleInfo.getStatusRunner().getArgs());
                        cmdCommand.setCommands(commandList);
                        Timeout timeout = new Timeout(Duration.create(30, TimeUnit.SECONDS));
                        Future<Object> execFuture = Patterns.ask(execCmdActor, cmdCommand, timeout);
                        try {
                            ExecResult execResult = (ExecResult) Await.result(execFuture, timeout.duration());
                            if (execResult.getExecResult()) {
                                recoverAlert(roleInstanceEntity, roleInstanceService);
                            } else {
                                saveAlert(roleInstanceEntity, roleInstanceService);
                            }
                        } catch (Exception e) {
                            //save alert
                            saveAlert(roleInstanceEntity, roleInstanceService);
                        }
                    }
                    //check namenode ha
                }
            }
        } else {
            unhandled(msg);
        }
    }

    private void recoverAlert(ClusterServiceRoleInstanceEntity roleInstanceEntity, ClusterServiceRoleInstanceService roleInstanceService) {
        ClusterAlertHistoryService alertHistoryService = SpringTool.getApplicationContext().getBean(ClusterAlertHistoryService.class);
        ClusterAlertHistory clusterAlertHistory = alertHistoryService.getOne(new QueryWrapper<ClusterAlertHistory>()
                .eq(Constants.ALERT_TARGET_NAME, roleInstanceEntity.getServiceRoleName() + " Survive")
                .eq(Constants.CLUSTER_ID, roleInstanceEntity.getClusterId())
                .eq(Constants.HOSTNAME, roleInstanceEntity.getHostname())
                .eq(Constants.IS_ENABLED, 1));
        if (Objects.nonNull(clusterAlertHistory)) {
            clusterAlertHistory.setIsEnabled(2);
            alertHistoryService.updateById(clusterAlertHistory);
        }
        //update service role instance state
        if (roleInstanceEntity.getServiceRoleState() != ServiceRoleState.RUNNING) {
            roleInstanceEntity.setServiceRoleState(ServiceRoleState.RUNNING);
            roleInstanceService.updateById(roleInstanceEntity);
        }
    }

    private void saveAlert(ClusterServiceRoleInstanceEntity roleInstanceEntity, ClusterServiceRoleInstanceService roleInstanceService) {
        ClusterAlertHistoryService alertHistoryService = SpringTool.getApplicationContext().getBean(ClusterAlertHistoryService.class);
        ClusterServiceInstanceService serviceInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceInstanceService.class);
        ClusterAlertHistory clusterAlertHistory = alertHistoryService.getOne(new QueryWrapper<ClusterAlertHistory>()
                .eq(Constants.ALERT_TARGET_NAME, roleInstanceEntity.getServiceRoleName() + " Survive")
                .eq(Constants.CLUSTER_ID, roleInstanceEntity.getClusterId())
                .eq(Constants.HOSTNAME, roleInstanceEntity.getHostname())
                .eq(Constants.IS_ENABLED, 1));

        ClusterServiceInstanceEntity serviceInstanceEntity = serviceInstanceService.getById(roleInstanceEntity.getServiceId());
        if (Objects.isNull(clusterAlertHistory)) {
            clusterAlertHistory = new ClusterAlertHistory();
            clusterAlertHistory.setClusterId(roleInstanceEntity.getClusterId());

            clusterAlertHistory.setAlertGroupName(roleInstanceEntity.getServiceName().toLowerCase());
            clusterAlertHistory.setAlertTargetName(roleInstanceEntity.getServiceRoleName() + " Survive");
            clusterAlertHistory.setCreateTime(new Date());
            clusterAlertHistory.setUpdateTime(new Date());
            clusterAlertHistory.setAlertLevel(AlertLevel.EXCEPTION);
            clusterAlertHistory.setAlertInfo("");
            clusterAlertHistory.setAlertAdvice("restart");
            clusterAlertHistory.setHostname(roleInstanceEntity.getHostname());
            clusterAlertHistory.setIsEnabled(1);

            clusterAlertHistory.setServiceInstanceId(roleInstanceEntity.getServiceId());

            alertHistoryService.save(clusterAlertHistory);
        }
        //update service role instance state
        serviceInstanceEntity.setServiceState(ServiceState.EXISTS_EXCEPTION);
        serviceInstanceService.updateById(serviceInstanceEntity);

        roleInstanceEntity.setServiceRoleState(ServiceRoleState.STOP);
        roleInstanceService.updateById(roleInstanceEntity);

    }
}
