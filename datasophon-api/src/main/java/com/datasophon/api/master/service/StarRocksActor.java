package com.datasophon.api.master.service;

import akka.actor.UntypedActor;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.command.GenerateStarRocksHAMessage;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;

import java.util.List;

public class StarRocksActor extends UntypedActor {
    @Override
    public void onReceive(Object msg) throws Throwable, Throwable {
        if(msg instanceof GenerateStarRocksHAMessage){
            GenerateStarRocksHAMessage message = (GenerateStarRocksHAMessage) msg;
            ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
            List<ClusterServiceRoleInstanceEntity> roleInstanceList = roleInstanceService.getRunningServiceRoleInstanceListByServiceId(message.getServiceInstanceId());
        }else {
            unhandled(msg);
        }
    }
}
