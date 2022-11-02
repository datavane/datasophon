package com.datasophon.worker.actor;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import cn.hutool.cron.CronUtil;
import com.datasophon.common.command.ServiceCheckCommand;
import com.datasophon.common.utils.PropertyUtils;
import com.datasophon.worker.task.ServiceCheckTask;

public class ServiceCheckActor extends UntypedActor {
    private ActorSelection masterActor;

    public ServiceCheckActor() {
        String masterHost = PropertyUtils.getString("masterHost");
        ActorSelection masterActor = context().actorSelection("akka.tcp://ddh@" + masterHost + ":2551/user/master");
    }

    @Override
    public void onReceive(Object message) throws Throwable, Throwable {

        if(message instanceof ServiceCheckCommand){
            ServiceCheckCommand command = (ServiceCheckCommand) message;
            CronUtil.schedule("15 * * * * ? ", new ServiceCheckTask(command,masterActor));
        }else {
            unhandled(message);
        }
    }
}
