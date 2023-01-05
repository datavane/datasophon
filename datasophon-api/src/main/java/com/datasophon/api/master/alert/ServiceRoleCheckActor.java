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

import java.util.*;

public class ServiceRoleCheckActor extends UntypedActor {

    private static final String promQl = "up{job=\"{}\",instance=\"{}.{}\"}";

    ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
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

            List<ClusterServiceRoleInstanceEntity> list = roleInstanceService.list(new QueryWrapper<ClusterServiceRoleInstanceEntity>()
                    .in(Constants.SERVICE_ROLE_NAME, "Prometheus", "AlertManager","FE","BE"));
            if (Objects.nonNull(list) && list.size() > 0) {
                for (ClusterServiceRoleInstanceEntity roleInstanceEntity : list) {
                    if("Prometheus".equals(roleInstanceEntity.getServiceRoleName())){
                        String url = "http://"+roleInstanceEntity.getHostname()+":9090";
                        try {
                            HttpUtil.get(url);
                            //recover alert
                            recoverAlert(roleInstanceEntity);
                        }catch (Exception e){
                            String alertTargetName = roleInstanceEntity.getServiceRoleName() + " Survive";
                            saveAlert(roleInstanceEntity,alertTargetName,AlertLevel.EXCEPTION,"restart");
                        }
                    }
                    if("AlertManager".equals(roleInstanceEntity.getServiceRoleName())){
                        String url = "http://"+roleInstanceEntity.getHostname()+":9093";
                        try {
                            HttpUtil.get(url);
                            recoverAlert(roleInstanceEntity);
                        }catch (Exception e){
                            //save alert
                            String alertTargetName = roleInstanceEntity.getServiceRoleName() + " Survive";
                            saveAlert(roleInstanceEntity,alertTargetName,AlertLevel.EXCEPTION,"restart");

                        }
                    }
                    if("FE".equals(roleInstanceEntity.getServiceRoleName()) ||
                            "BE".equals(roleInstanceEntity.getServiceRoleName())){
                        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+roleInstanceEntity.getClusterId());
                        String feMaster = globalVariables.get("${feMaster}");
                        List<ProcInfo> procInfos = new ArrayList<>();
                        String serviceRoleName = roleInstanceEntity.getServiceRoleName();
                        if("FE".equals(roleInstanceEntity.getServiceRoleName()) ){
                            procInfos = StarRocksUtils.showFrontends(feMaster);
                        }else{
                            procInfos = StarRocksUtils.showBackends(feMaster);
                        }
                        for (ProcInfo frontend : procInfos) {
                            if(!frontend.getAlive()){
                                String alertTargetName = serviceRoleName+" Alive";
                                String alertAdvice = "the errmsg is "+frontend.getErrMsg();
                                saveAlert(roleInstanceEntity,alertTargetName,AlertLevel.WARN,alertAdvice);
                            }else {
                                recoverAlert(roleInstanceEntity);
                            }
                        }
                    }

                }
            }
        } else {
            unhandled(msg);
        }
    }

    private void recoverAlert(ClusterServiceRoleInstanceEntity roleInstanceEntity) {
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

    private void saveAlert(ClusterServiceRoleInstanceEntity roleInstanceEntity,String alertTargetName,AlertLevel alertLevel,String alertAdvice) {
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
            clusterAlertHistory.setAlertTargetName(alertTargetName);
            clusterAlertHistory.setCreateTime(new Date());
            clusterAlertHistory.setUpdateTime(new Date());
            clusterAlertHistory.setAlertLevel(alertLevel);
            clusterAlertHistory.setAlertInfo("");
            clusterAlertHistory.setAlertAdvice(alertAdvice);
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
