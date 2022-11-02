package com.datasophon.worker.actor;

import akka.actor.*;
import com.alibaba.fastjson.JSONObject;
import com.datasophon.common.model.StartWorkerMessage;
import com.datasophon.common.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WorkerActor extends UntypedActor {
    private static final Logger logger = LoggerFactory.getLogger(WorkerActor.class);

    private String masterHost;

    private String hostname;

    private ActorSelection masterActor;


    private Map<String,String> commandMap;


    public WorkerActor(String hostname) {
        this.masterHost = PropertyUtils.getString("masterHost");
        this.hostname = hostname;
        this.masterActor = context().actorSelection("akka.tcp://ddh@" + masterHost + ":2551/user/master");
        commandMap = new ConcurrentHashMap<>();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) {
        logger.info("worker actor restart by reason {}",reason.getMessage());
    }

    @Override
    public void preStart() throws IOException {
        ActorRef installServiceActor = getContext().actorOf(Props.create(InstallServiceActor.class), "installServiceActor");
        ActorRef configureServiceActor = getContext().actorOf(Props.create(ConfigureServiceActor.class), "configureServiceActor");
        ActorRef startServiceActor = getContext().actorOf(Props.create(StartServiceActor.class), "startServiceActor");
        ActorRef stopServiceActor = getContext().actorOf(Props.create(StopServiceActor.class), "stopServiceActor");
        ActorRef restartServiceActor = getContext().actorOf(Props.create(RestartServiceActor.class), "restartServiceActor");
        ActorRef logActor = getContext().actorOf(Props.create(LogActor.class), "logActor");
        ActorRef executeCmdActor = getContext().actorOf(Props.create(ExecuteCmdActor.class), "executeCmdActor");
        ActorRef fileOperateActor = getContext().actorOf(Props.create(FileOperateActor.class), "fileOperateActor");
        ActorRef alertConfigActor = getContext().actorOf(Props.create(AlertConfigActor.class), "alertConfigActor");
        getContext().watch(installServiceActor);
        getContext().watch(configureServiceActor);
        getContext().watch(startServiceActor);
        getContext().watch(stopServiceActor);
        getContext().watch(restartServiceActor);
        getContext().watch(logActor);
        getContext().watch(executeCmdActor);
        getContext().watch(fileOperateActor);
        getContext().watch(alertConfigActor);
    }



    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            if("start".equals(message)){
//                ExecResult result = ShellUtils.exceShell("/opt/datasophon/ddh-worker-1.0.0/ddh-worker/script/host-info-collect.sh");
//                logger.info("host info collect result:{}", result);
//                final StartWorkerMessage startWorkerMessage = JSONObject.parseObject(result.getExecOut(), StartWorkerMessage.class);
                String str = "{coreNum: 8, totalMem: 31.4189, totalDisk: 991.51,usedDisk: 9.59, diskAvail: 981.92,usedMem:5.91602,memUsedPersent:18.8295,diskUsedPersent:1.0,averageLoad:0.06}";
                StartWorkerMessage startWorkerMessage = JSONObject.parseObject(str, StartWorkerMessage.class);
                startWorkerMessage.setClusterId(PropertyUtils.getInt("clusterId"));
                startWorkerMessage.setHostname(hostname);
                masterActor.tell(message, ActorRef.noSender());
                logger.info("send worker start message to master");
            }else{
                logger.info("receive message {}",message);
            }
        }  else if(message instanceof Terminated){
            Terminated t = (Terminated) message;
            logger.info("find actor {} terminated",t.getActor());
        }
        else {
            unhandled(message);
        }
    }


    public static void main(String[] args) {
        String str = "{coreNum: 8, totalMem: 31.4189, totalDisk: 991.51,usedDisk: 9.59, diskAvail: 981.92,usedMem:5.91602,memUsedPersent:18.8295,diskUsedPersent:1.0,averageLoad:0.06}";
        StartWorkerMessage message = JSONObject.parseObject(str, StartWorkerMessage.class);
    }

}
