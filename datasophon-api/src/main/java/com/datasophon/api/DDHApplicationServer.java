package com.datasophon.api;

import com.datasophon.api.master.ActorUtils;
import com.datasophon.common.cache.CacheUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@ServletComponentScan
@ComponentScan("com.datasophon")
@MapperScan("com.datasophon.dao")
public class DDHApplicationServer extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(DDHApplicationServer.class);

    public static void main(String[] args) throws UnknownHostException  {
        //SpringApplication.run(DDHApplicationServer.class, args);
        SpringApplication app = new SpringApplication(DDHApplicationServer.class);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "External: \t{}://{}:{}\n\t",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    @PostConstruct
    public void run() throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        CacheUtils.put("hostname",hostName);
        ActorUtils.init();
    }
}
