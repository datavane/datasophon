package com.datasophon.api;
import akka.actor.*;
import com.datasophon.api.master.MasterServer;
import com.datasophon.api.master.alert.HostCheckActor;
import com.datasophon.api.master.alert.ServiceRoleCheckActor;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.HostCheckCommand;
import com.datasophon.common.command.ServiceRoleCheckCommand;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.PostConstruct;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

@SpringBootApplication
@ServletComponentScan
@ComponentScan("com.datasophon.")
@MapperScan("com.datasophon..dao")
public class DDHApplicationServer extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DDHApplicationServer.class,args);
    }
    @PostConstruct
    public void run() throws UnknownHostException {

        String hostname = InetAddress.getLocalHost().getHostName();
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostname);
        ActorSystem system = ActorSystem.create("datasophon",config.withFallback(ConfigFactory.load()));

        ActorRef master = system.actorOf(Props.create(MasterServer.class), "master");
        ActorRef serviceRoleCheckActor = system.actorOf(Props.create(ServiceRoleCheckActor.class), "serviceRoleCheckActor");
        ActorRef hostCheckActor = system.actorOf(Props.create(HostCheckActor.class), "hostCheckActor");
//        ActorSelection actorSelection = system.actorSelection("akka.tcp://ddh@ddh01:2552/user/worker");
//        actorSelection.tell("hello world",ActorRef.noSender());
//        system.scheduler().schedule(FiniteDuration.apply(10L,TimeUnit.SECONDS), FiniteDuration.apply(10L, TimeUnit.SECONDS), serviceRoleCheckActor, new ServiceRoleCheckCommand(), system.dispatcher(), ActorRef.noSender());
        system.scheduler().schedule(FiniteDuration.apply(60L,TimeUnit.SECONDS), FiniteDuration.apply(5L, TimeUnit.MINUTES), hostCheckActor, new HostCheckCommand(), system.dispatcher(), ActorRef.noSender());

        system.scheduler().schedule(FiniteDuration.apply(15L,TimeUnit.SECONDS), FiniteDuration.apply(1L, TimeUnit.MINUTES), serviceRoleCheckActor, new ServiceRoleCheckCommand(), system.dispatcher(), ActorRef.noSender());

        CacheUtils.put("actorSystem",system);
        CacheUtils.put("hostname",hostname);
        CacheUtils.put("masterActor",master);

    }
}
