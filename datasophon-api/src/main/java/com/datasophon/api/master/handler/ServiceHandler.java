package com.datasophon.api.master.handler;

import akka.actor.ActorSystem;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.ServiceRoleInfo;
import com.datasophon.common.utils.ExecResult;
import lombok.Data;

@Data
public abstract class ServiceHandler {

    private ServiceHandler next;

    ActorSystem actorSystem = (ActorSystem) CacheUtils.get("actorSystem");

    public abstract ExecResult handlerRequest(ServiceRoleInfo serviceRoleInfo ) throws Exception;

}
