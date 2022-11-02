package com.datasophon.api.master.handler;

import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.GenerateServiceConfigCommand;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.common.utils.ExecResult;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Objects;

public class ServiceConfigureHandler extends ServiceHandler{
    @Override
    public ExecResult handlerRequest(ServiceRoleInfo serviceRoleInfo) throws Exception {
        //配置
        GenerateServiceConfigCommand generateServiceConfigCommand = new GenerateServiceConfigCommand();
        generateServiceConfigCommand.setServiceName(serviceRoleInfo.getParentName());
        generateServiceConfigCommand.setCofigFileMap(serviceRoleInfo.getConfigFileMap());
        generateServiceConfigCommand.setDecompressPackageName(serviceRoleInfo.getDecompressPackageName());
        if("zkserver".equals(serviceRoleInfo.getName().toLowerCase())){
            generateServiceConfigCommand.setMyid((Integer) CacheUtils.get("zkserver_"+serviceRoleInfo.getHostname()));
        }
        generateServiceConfigCommand.setServiceRoleName(serviceRoleInfo.getName());
        ActorSelection configActor = actorSystem.actorSelection("akka.tcp://ddh@" + serviceRoleInfo.getHostname() + ":2552/user/worker/configureServiceActor");
//        if(serviceRoleInfo.getRoleType() == ServiceRoleType.CLIENT){
//            ExecResult execResult = new ExecResult();
//            execResult.setExecResult(true);
//            return execResult;
//        }
        Timeout timeout = new Timeout(Duration.create(180, "seconds"));
        Future<Object> configureFuture = Patterns.ask(configActor, generateServiceConfigCommand, timeout);
        ExecResult configResult = (ExecResult) Await.result(configureFuture, timeout.duration());
        if(Objects.nonNull(configResult) && configResult.getExecResult()){
            if(Objects.nonNull(getNext()) ){
                return getNext().handlerRequest(serviceRoleInfo);
            }
        }
        return configResult;
    }
}
