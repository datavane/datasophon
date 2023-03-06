package com.datasophon.api.service.impl;

import com.datasophon.api.service.SystemSettingService;
import com.datasophon.common.utils.ShellUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service("SystemSettingService")
public class SystemSettingServiceImpl implements SystemSettingService {
    private static final Logger logger = LoggerFactory.getLogger(SystemSettingServiceImpl.class);
    @Override
    public void powerOn(Boolean isPowerOn) throws IOException {
        //todo 设置状态

        //todo 创建脚本
        File file = new File("/etc/profile.d/enable_start_datasophon.sh");
        Path path = file.toPath();
        ShellUtils.addChmod(file.getPath(),"-x");


        if (Files.exists(path)) {

        }

        logger.info(System.getProperty("user.dir"));
        StringBuilder sb = new StringBuilder();
        sb.append("#!/bin/sh\n");
        sb.append(System.getProperty("user.dir"));
        sb.append("/bin/datasophon-api.sh start api");

        // write data to file
        FileUtils.writeStringToFile(file, sb.toString(), StandardCharsets.UTF_8);

        //todo 添加执行权限


    }


    public static void main(String[] args) throws IOException {

        File file = new File("D:\\pro\\datasophon\\enable_start_datasophon.sh");
        Path path = file.toPath();

        if (Files.exists(path)) {

        }

        System.out.println(System.getProperty("user.dir"));

        StringBuilder sb = new StringBuilder();
        sb.append("#!/bin/sh\n");
        sb.append(System.getProperty("user.dir"));
        sb.append("/bin/datasophon-api.sh start api");

        // write data to file
        FileUtils.writeStringToFile(file, sb.toString(), StandardCharsets.UTF_8);


    }
}