package com.datasophon.worker.strategy;

import com.datasophon.worker.utils.TaskConstants;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class AbstractHandlerStrategy {
    public String serviceName;

    public String serviceRoleName;

    public Logger logger;

    public AbstractHandlerStrategy(String serviceName,String serviceRoleName) {
        this.serviceName = serviceName;
        this.serviceRoleName = serviceRoleName;
        String loggerName = String.format("%s-%s-%s", TaskConstants.TASK_LOG_LOGGER_NAME, serviceName, serviceRoleName);
        logger = LoggerFactory.getLogger(loggerName);
    }

}
