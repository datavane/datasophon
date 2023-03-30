/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.datasophon.api.service.impl;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import cn.hutool.core.io.FileUtil;
import com.datasophon.api.exceptions.ServiceException;
import com.datasophon.api.master.ActorUtils;
import com.datasophon.api.service.ClusterKerberosService;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.remote.GenerateKeytabFileCommand;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Service("clusterKerberosService")
@Transactional
public class ClusterKerberosServiceImpl implements ClusterKerberosService {


    @Autowired
    private ClusterServiceRoleInstanceService roleInstanceService;

    @Override
    public void downloadKeytab(Integer clusterId, String principal, String keytabName, String hostname, HttpServletResponse response) throws IOException {
        String userDir = System.getProperty("user.dir");
        String keytabFilePath = "/etc/security/keytab"+ Constants.SLASH + hostname + Constants.SLASH + keytabName;
        File file = new File(keytabFilePath);
        if (!file.exists()) {
            generateKeytabFile(clusterId, keytabFilePath, principal, keytabName,hostname);
        }
        FileInputStream inputStream = new FileInputStream(file);

        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Length", "" + file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + keytabName);
        OutputStream out = response.getOutputStream();
        int length = 0;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        inputStream.close();
        out.flush();
        out.close();
    }

    @Override
    public void uploadKeytab(MultipartFile file, String hostname, String keytabFileName) throws IOException {
        String keytabFilePath = "/etc/security/keytab"+ Constants.SLASH + hostname + Constants.SLASH + keytabFileName;
        file.transferTo(new File(keytabFilePath));
    }



    private void generateKeytabFile(Integer clusterId, String keytabFilePath, String principal, String keytabName,String hostname) {
        ClusterServiceRoleInstanceEntity roleInstanceEntity = roleInstanceService.getKAdminRoleIns(clusterId);
        ActorRef kerberosActor = ActorUtils.getRemoteActor(roleInstanceEntity.getHostname(), "kerberosActor");
        GenerateKeytabFileCommand command = new GenerateKeytabFileCommand();
        command.setKeytabName(keytabName);
        command.setPrincipal(principal);
        command.setHostname(hostname);
        Timeout timeout = new Timeout(Duration.create(180, TimeUnit.SECONDS));
        Future<Object> execFuture = Patterns.ask(kerberosActor, command, timeout);
        ExecResult execResult = null;
        try {
            execResult = (ExecResult) Await.result(execFuture, timeout.duration());
            String localHostname = CacheUtils.getString(Constants.HOSTNAME);
            if (execResult.getExecResult() && !localHostname.equals(roleInstanceEntity.getHostname())) {
                String keytabFileDir = "/etc/security/keytab"+ Constants.SLASH + hostname + Constants.SLASH;
                ShellUtils.exceShell("scp root@"+roleInstanceEntity.getHostname()+":"+keytabFilePath+" "+keytabFileDir);
            }
        } catch (Exception e) {

        }
    }
}
