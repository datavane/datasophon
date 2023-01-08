package com.datasophon.api.master.alert;

import akka.actor.UntypedActor;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.service.ClusterAlertHistoryService;
import com.datasophon.api.service.ClusterServiceInstanceService;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.ServiceRoleCheckCommand;
import com.datasophon.common.model.ProcInfo;
import com.datasophon.common.utils.StarRocksUtils;
import com.datasophon.dao.entity.ClusterAlertHistory;
import com.datasophon.dao.entity.ClusterServiceInstanceEntity;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.AlertLevel;
import com.datasophon.dao.enums.ServiceRoleState;
import com.datasophon.dao.enums.ServiceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ServiceRoleCheckActor extends UntypedActor {

    private static final String promQl = "up{job=\"{}\",instance=\"{}.{}\"}";

    private static final Logger logger = LoggerFactory.getLogger(ServiceRoleCheckActor.class);


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
        if (msg instanceof ServiceRoleCheckCommand) {
            ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
            List<ClusterServiceRoleInstanceEntity> list = roleInstanceService.list(new QueryWrapper<ClusterServiceRoleInstanceEntity>()
                    .in(Constants.SERVICE_ROLE_NAME, "Prometheus", "AlertManager", "FE", "BE"));
            Map<String, ClusterServiceRoleInstanceEntity> map = list.stream().collect(Collectors.toMap(e -> e.getHostname() + e.getServiceRoleName(), e -> e, (v1, v2) -> v1));
            if (Objects.nonNull(list) && list.size() > 0) {
                for (ClusterServiceRoleInstanceEntity roleInstanceEntity : list) {
                    if ("Prometheus".equals(roleInstanceEntity.getServiceRoleName())) {
                        String url = "http://" + roleInstanceEntity.getHostname() + ":9090";
                        try {
                            HttpUtil.get(url);
                            //recover alert
                            recoverAlert(roleInstanceEntity, roleInstanceEntity.getHostname());
                        } catch (Exception e) {
                            String alertTargetName = roleInstanceEntity.getServiceRoleName() + " Survive";
                            saveAlert(roleInstanceEntity, roleInstanceEntity.getHostname(), alertTargetName, AlertLevel.EXCEPTION, "restart");
                        }
                    }
                    if ("AlertManager".equals(roleInstanceEntity.getServiceRoleName())) {
                        String url = "http://" + roleInstanceEntity.getHostname() + ":9093";
                        try {
                            HttpUtil.get(url);
                            recoverAlert(roleInstanceEntity, roleInstanceEntity.getHostname());
                        } catch (Exception e) {
                            //save alert
                            String alertTargetName = roleInstanceEntity.getServiceRoleName() + " Survive";
                            saveAlert(roleInstanceEntity, roleInstanceEntity.getHostname(), alertTargetName, AlertLevel.EXCEPTION, "restart");

                        }
                    }
                    if ("FE".equals(roleInstanceEntity.getServiceRoleName())) {
                        Map<String, String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables" + Constants.UNDERLINE + roleInstanceEntity.getClusterId());
                        String feMaster = globalVariables.get("${feMaster}");
                        if (roleInstanceEntity.getHostname().equals(feMaster) && roleInstanceEntity.getServiceRoleState() == ServiceRoleState.RUNNING) {

                            List<ProcInfo> frontends = StarRocksUtils.showFrontends(feMaster);

                            List<ProcInfo> backends = StarRocksUtils.showBackends(feMaster);

                            resolveProcInfoAlert("FE", frontends, map);
                            resolveProcInfoAlert("BE", backends, map);
                        }
                    }

                }
            }
        } else {
            unhandled(msg);
        }
    }

    private void resolveProcInfoAlert(String serviceRoleName, List<ProcInfo> frontends, Map<String, ClusterServiceRoleInstanceEntity> map) {
        for (ProcInfo frontend : frontends) {
            ClusterServiceRoleInstanceEntity roleInstanceEntity = map.get(frontend.getHostName() + serviceRoleName);
//            ClusterServiceRoleInstanceEntity roleInstanceEntity = roleInstanceService.getServiceRoleInsByHostAndName(frontend.getHostName(), serviceRoleName);
            if (!frontend.getAlive()) {
                String alertTargetName = serviceRoleName + " Alive";
                logger.info("{} at host {} is not alive", serviceRoleName, frontend.getHostName());
                String alertAdvice = "the errmsg is " + frontend.getErrMsg();
                saveAlert(roleInstanceEntity, frontend.getHostName(), alertTargetName, AlertLevel.WARN, alertAdvice);
            } else {
                recoverAlert(roleInstanceEntity, frontend.getHostName());
            }
        }
    }

    private void recoverAlert(ClusterServiceRoleInstanceEntity roleInstanceEntity, String hostname) {
        ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
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

    private void saveAlert(ClusterServiceRoleInstanceEntity roleInstanceEntity, String hostname, String alertTargetName, AlertLevel alertLevel, String alertAdvice) {
        ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
        ClusterAlertHistoryService alertHistoryService = SpringTool.getApplicationContext().getBean(ClusterAlertHistoryService.class);
        ClusterServiceInstanceService serviceInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceInstanceService.class);
        ClusterAlertHistory clusterAlertHistory = alertHistoryService.getOne(new QueryWrapper<ClusterAlertHistory>()
                .eq(Constants.ALERT_TARGET_NAME, alertTargetName)
                .eq(Constants.CLUSTER_ID, roleInstanceEntity.getClusterId())
                .eq(Constants.HOSTNAME, roleInstanceEntity.getHostname())
                .eq(Constants.IS_ENABLED, 1));

        ClusterServiceInstanceEntity serviceInstanceEntity = serviceInstanceService.getById(roleInstanceEntity.getServiceId());
        if (Objects.isNull(clusterAlertHistory)) {
            clusterAlertHistory = new ClusterAlertHistory();
            clusterAlertHistory.setClusterId(roleInstanceEntity.getClusterId());

            clusterAlertHistory.setAlertGroupName(roleInstanceEntity.getServiceName().toLowerCase());
            clusterAlertHistory.setAlertTargetName(alertTargetName);
            clusterAlertHistory.setCreateTime(new Date());
            clusterAlertHistory.setUpdateTime(new Date());
            clusterAlertHistory.setAlertLevel(alertLevel);
            clusterAlertHistory.setAlertInfo("");
            clusterAlertHistory.setAlertAdvice(alertAdvice);
            clusterAlertHistory.setHostname(roleInstanceEntity.getHostname());
            clusterAlertHistory.setServiceRoleInstanceId(roleInstanceEntity.getId());
            clusterAlertHistory.setServiceInstanceId(roleInstanceEntity.getServiceId());
            clusterAlertHistory.setIsEnabled(1);

            clusterAlertHistory.setServiceInstanceId(roleInstanceEntity.getServiceId());

            alertHistoryService.save(clusterAlertHistory);
        }
        //update service role instance state
        serviceInstanceEntity.setServiceState(ServiceState.EXISTS_EXCEPTION);
        roleInstanceEntity.setServiceRoleState(ServiceRoleState.STOP);
        if (alertLevel == AlertLevel.WARN) {
            serviceInstanceEntity.setServiceState(ServiceState.EXISTS_ALARM);
            roleInstanceEntity.setServiceRoleState(ServiceRoleState.EXISTS_ALARM);
        }
        serviceInstanceService.updateById(serviceInstanceEntity);
        roleInstanceService.updateById(roleInstanceEntity);

    }
}
