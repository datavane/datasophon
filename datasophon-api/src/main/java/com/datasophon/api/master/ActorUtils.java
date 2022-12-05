package com.datasophon.api.master;

import akka.actor.*;
import akka.util.Timeout;
import com.datasophon.api.master.alert.HostCheckActor;
import com.datasophon.api.master.alert.ServiceRoleCheckActor;
import com.datasophon.common.command.HostCheckCommand;
import com.datasophon.common.command.ServiceRoleCheckCommand;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ActorUtils {
    private static final Logger logger = LoggerFactory.getLogger(ActorUtils.class);

    public static ActorSystem actorSystem;

    private ActorUtils() {

    }

    public static void init() throws UnknownHostException {
        String hostname = InetAddress.getLocalHost().getHostName();
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostname);
        actorSystem = ActorSystem.create("datasophon", config.withFallback(ConfigFactory.load()));
        actorSystem.actorOf(Props.create(MasterServer.class), "master");
        ActorRef serviceRoleCheckActor = actorSystem.actorOf(Props.create(ServiceRoleCheckActor.class), "serviceRoleCheckActor");
        ActorRef hostCheckActor = actorSystem.actorOf(Props.create(HostCheckActor.class), "hostCheckActor");

        actorSystem.scheduler().schedule(FiniteDuration.apply(60L, TimeUnit.SECONDS),
                FiniteDuration.apply(5L, TimeUnit.MINUTES),
                hostCheckActor, new HostCheckCommand(),
                actorSystem.dispatcher(),
                ActorRef.noSender());

        actorSystem.scheduler().schedule(FiniteDuration.apply(15L, TimeUnit.SECONDS),
                FiniteDuration.apply(1L, TimeUnit.MINUTES),
                serviceRoleCheckActor,
                new ServiceRoleCheckCommand(),
                actorSystem.dispatcher(),
                ActorRef.noSender());
    }

    public static ActorRef getLocalActor(Class actorClass, String actorName) {
        ActorSelection actorSelection = actorSystem.actorSelection("/user/" + actorName);
        Timeout timeout = new Timeout(Duration.create(30, TimeUnit.SECONDS));
        Future<ActorRef> future = actorSelection.resolveOne(timeout);
        ActorRef actorRef = null;
        try {
            actorRef = Await.result(future, Duration.create(30, TimeUnit.SECONDS));
        } catch (Exception e) {
            logger.error("{} actor not found",actorName);
        }
        if (Objects.isNull(actorRef)) {
            logger.info("create actor {}",actorName);
            actorRef = actorSystem.actorOf(Props.create(actorClass).withDispatcher("my-forkjoin-dispatcher"), actorName);
        }else{
            logger.info("find actor {}",actorName);
        }
        return actorRef;
    }

    public static ActorRef getRemoteActor(String hostname, String actorName) {
        String actorPath = "akka.tcp://datasophon@" + hostname + ":2552/user/worker/"+actorName;
        ActorSelection actorSelection = actorSystem.actorSelection(actorPath);
        Timeout timeout = new Timeout(Duration.create(30, TimeUnit.SECONDS));
        Future<ActorRef> future = actorSelection.resolveOne(timeout);
        ActorRef actorRef = null;
        try {
            actorRef = Await.result(future, Duration.create(30, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actorRef;
    }
}
