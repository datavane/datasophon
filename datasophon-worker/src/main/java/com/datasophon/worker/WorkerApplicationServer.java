package com.datasophon.worker;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.EventStream;
import akka.remote.AssociatedEvent;
import akka.remote.AssociationErrorEvent;
import akka.remote.DisassociatedEvent;
import com.alibaba.fastjson.JSONObject;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.lifecycle.ServerLifeCycleManager;
import com.datasophon.common.model.StartWorkerMessage;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.PropertyUtils;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.worker.actor.RemoteEventActor;
import com.datasophon.worker.actor.WorkerActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class WorkerApplicationServer  {

    private static final Logger logger = LoggerFactory.getLogger(WorkerApplicationServer.class);

    public static void main(String[] args) throws UnknownHostException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, MalformedObjectNameException {
        //actorsystem
        String hostname = InetAddress.getLocalHost().getHostName();
        CacheUtils.put("hostname",hostname);
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=" + hostname);
        ActorSystem system = ActorSystem.create("datasophon", config.withFallback(ConfigFactory.load()));
        //init actor
        system.actorOf(Props.create(WorkerActor.class), "worker");

        ActorRef remoteEventActor = system.actorOf(Props.create(RemoteEventActor.class), "remoteEventActor");

        //订阅远程监听事件
        EventStream eventStream = system.eventStream();
        eventStream.subscribe(remoteEventActor, AssociationErrorEvent.class);
        eventStream.subscribe(remoteEventActor, AssociatedEvent.class);
        eventStream.subscribe(remoteEventActor, DisassociatedEvent.class);

        String masterHost = PropertyUtils.getString("masterHost");

        ActorSelection workerStartActor = system.actorSelection("akka.tcp://datasophon@" + masterHost + ":2551/user/workerStartActor");

        String workDir = System.getProperty("user.dir");
        ExecResult result = ShellUtils.exceShell(workDir + "/script/host-info-collect.sh");
        logger.info("host info collect result:{}", result);
        StartWorkerMessage startWorkerMessage = JSONObject.parseObject(result.getExecOut(), StartWorkerMessage.class);
        String cpuArchitecture = ShellUtils.getCpuArchitecture();
        startWorkerMessage.setCpuArchitecture(cpuArchitecture);
        startWorkerMessage.setClusterId(PropertyUtils.getInt("clusterId"));
        startWorkerMessage.setHostname(hostname);


        ArrayList<String> commands = new ArrayList<>();
        commands.add("sh");
        if (Constants.x86_64.equals(cpuArchitecture)) {
            commands.add(workDir + "/node/x86/control.sh");
        } else {
            commands.add(workDir + "/node/arm/control.sh");
        }
        commands.add("restart");
        commands.add("node");
        ShellUtils.execWithStatus(Constants.INSTALL_PATH, commands, 60L);


        workerStartActor.tell(startWorkerMessage, ActorRef.noSender());
        logger.info("start worker");

        /*
         * registry hooks, which are called before the process exits
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!ServerLifeCycleManager.isStopped()) {
                close("WorkerServer shutdown hook");
            }
        }));

//        MBeanServer platformBeanServer = ManagementFactory.getPlatformMBeanServer();
//        EsMetrics esMetrics = new EsMetrics();
//        ObjectName objectName = new ObjectName("com.datasophon.ddh.worker.metrics:type=esMetrics");
//        platformBeanServer.registerMBean(esMetrics, objectName);

    }

      public static void close(String cause) {
        logger.info("Worker server stopped, current cause: {}", cause);
        String workDir = System.getProperty("user.dir");
        String cpuArchitecture = ShellUtils.getCpuArchitecture();
        ArrayList<String> commands = new ArrayList<>();
        commands.add("sh");
        if (Constants.x86_64.equals(cpuArchitecture)) {
            commands.add(workDir + "/node/x86/control.sh");
        } else {
            commands.add(workDir + "/node/arm/control.sh");
        }
        commands.add("stop");
        commands.add("node");
        ShellUtils.execWithStatus(Constants.INSTALL_PATH, commands, 60L);
        logger.info("Worker server stopped, current cause: {}", cause);

    }

}
