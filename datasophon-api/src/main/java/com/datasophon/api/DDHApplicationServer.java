package com.datasophon.api;

import akka.actor.*;
import com.datasophon.api.master.ActorUtils;
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
@ComponentScan("com.datasophon")
@MapperScan("com.datasophon.dao")
public class DDHApplicationServer extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DDHApplicationServer.class, args);
    }

    @PostConstruct
    public void run() throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        CacheUtils.put("hostname",hostName);
        ActorUtils.init();
    }
}
