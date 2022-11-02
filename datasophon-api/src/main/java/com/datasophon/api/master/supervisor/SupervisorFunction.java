package com.datasophon.api.master.supervisor;


import akka.actor.SupervisorStrategy;
import akka.japi.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SupervisorFunction implements Function<Throwable, SupervisorStrategy.Directive> {

    private static final Logger logger = LoggerFactory.getLogger(SupervisorFunction.class);

    @Override
    public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
        if(param instanceof ArithmeticException){
            logger.info("meet ArithmeticException,just resume");
            return SupervisorStrategy.resume();
        }else if(param instanceof  NullPointerException){
            logger.info("meet NullPointerException,restart");
            return SupervisorStrategy.restart();
        }else if(param instanceof IllegalArgumentException){
            return SupervisorStrategy.stop();
        } else if(param instanceof SQLException){
            logger.info("meet SQLException,restart");
            return SupervisorStrategy.restart();
        }else {
            return SupervisorStrategy.restart();
        }
    }
}
