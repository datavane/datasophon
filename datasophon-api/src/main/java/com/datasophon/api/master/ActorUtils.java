package com.datasophon.api.master;

import akka.actor.ActorNotFound;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.util.Timeout;
import com.datasophon.common.cache.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ActorUtils {
    private static final Logger logger = LoggerFactory.getLogger(ActorUtils.class);


    public static ActorRef getActor(String actorName){
        ActorSystem system = (ActorSystem) CacheUtils.get("actorSystem");
        ActorSelection actorSelection = system.actorSelection("/user/" + actorName);
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        Future<ActorRef> future = actorSelection.resolveOne(timeout);
        future.onSuccess(new OnSuccess<ActorRef>() {
            @Override
            public void onSuccess(ActorRef actorRef) throws Throwable, Throwable {
                logger.info("find actor {}",actorRef.path().toString());

            }
        },system.dispatcher());

        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable ex) throws Throwable, Throwable {
                if(ex instanceof ActorNotFound){
                    logger.info("actor is not exists,create it");
                }
            }
        },system.dispatcher());
        try {
            ActorRef actorRef = Await.result(future, Duration.create(2, "seconds"));
            return actorRef;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
