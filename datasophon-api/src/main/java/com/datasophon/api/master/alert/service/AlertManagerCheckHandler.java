package com.datasophon.api.master.alert.service;

import cn.hutool.http.HttpUtil;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.model.HostInfo;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import org.apache.sshd.client.session.ClientSession;

import java.util.List;

public class AlertManagerCheckHandler implements ServiceCheckHandler{
    @Override
    public void handle() {
        ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
        List<ClusterServiceRoleInstanceEntity> list = roleInstanceService.listServiceRoleByName("AlertManager");
        for (ClusterServiceRoleInstanceEntity roleInstanceEntity : list) {
            String url = "http://" + roleInstanceEntity.getHostname() + ":9093";
            try {
                HttpUtil.get(url);
                ProcessUtils.recoverAlert(roleInstanceEntity, roleInstanceService);
            } catch (Exception e) {
                //产生告警
                ProcessUtils.saveAlert(roleInstanceEntity, roleInstanceService);
            }
        }

    }
}
