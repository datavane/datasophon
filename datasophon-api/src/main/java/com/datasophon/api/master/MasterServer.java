package com.datasophon.api.master;

import akka.actor.*;
import com.datasophon.api.master.service.StarRocksActor;
import com.datasophon.api.master.supervisor.SupervisorFunction;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.model.StartWorkerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.concurrent.duration.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MasterServer extends UntypedActor {


    private static final Logger logger = LoggerFactory.getLogger(MasterServer.class);


    //定义监督策略
    private static SupervisorStrategy strategy = new OneForOneStrategy(3,
            Duration.create(1, TimeUnit.MINUTES),
            new SupervisorFunction()); //一分钟重试3次);

    public static SupervisorStrategy getStrategy() {
        return strategy;
    }


    @Override
    public void preStart() throws Exception {
        ActorRef serviceActor = getContext().actorOf(Props.create(MasterServiceActor.class).withDispatcher("my-pinned-dispatcher"), "serviceActor");
        ActorRef workerStartActor = getContext().actorOf(Props.create(WorkerStartActor.class).withDispatcher("my-pinned-dispatcher"), "workerStartActor");

        ActorRef dagBuildActor = getContext().actorOf(Props.create(DAGBuildActor.class).withDispatcher("my-pinned-dispatcher"), "dagBuildActor");
        ActorRef submitTaskNodeActor = getContext().actorOf(Props.create(SubmitTaskNodeActor.class).withDispatcher("my-pinned-dispatcher"), "submitTaskNodeActor");
        ActorRef serviceExecuteResultActor = getContext().actorOf(Props.create(ServiceExecuteResultActor.class).withDispatcher("my-pinned-dispatcher"), "serviceExecuteResultActor");
        ActorRef prometheusActor = getContext().actorOf(Props.create(PrometheusActor.class).withDispatcher("my-pinned-dispatcher"), "prometheusActor");
        ActorRef starRocksActor = getContext().actorOf(Props.create(StarRocksActor.class).withDispatcher("my-pinned-dispatcher"), "starRocksActor");
        ActorRef hdfsECActor = getContext().actorOf(Props.create(HdfsECActor.class).withDispatcher("my-pinned-dispatcher"), "hdfsECActor");
        getContext().watch(serviceActor);
        getContext().watch(workerStartActor);
        getContext().watch(dagBuildActor);
        getContext().watch(submitTaskNodeActor);
        getContext().watch(serviceExecuteResultActor);
        getContext().watch(starRocksActor);
        getContext().watch(hdfsECActor);
        CacheUtils.put("hostActor",workerStartActor);
        CacheUtils.put("serviceActor",serviceActor);
        CacheUtils.put("dagBuildActor",dagBuildActor);
        CacheUtils.put("submitTaskNodeActor",submitTaskNodeActor);
        CacheUtils.put("serviceExecuteResultActor",serviceExecuteResultActor);
        CacheUtils.put("prometheusActor",prometheusActor);
        CacheUtils.put("starRocksActor",starRocksActor);
        CacheUtils.put("hdfsECActor",hdfsECActor);
        super.preStart();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        logger.info("master actor restart because {}",reason.getMessage());
        super.preRestart(reason, message);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof StartWorkerMessage) {

        } else if(message instanceof Terminated){
            Terminated t = (Terminated) message;
            logger.info("actor {} terminated",t.getActor().path());

        }else if(message instanceof ActorIdentity){
            ActorIdentity ai = (ActorIdentity) message;
            ActorRef ref = ai.getRef();
            if(Objects.isNull(ref)){
                logger.info("node {} is stopped",ai.correlationId());
            }else{
                getContext().watch(ref);
            }
        }
        else {
            unhandled(message);
        }
    }


}
