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

package com.datasophon.api.controller;

import com.datasophon.api.service.ClusterKerberosService;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("cluster/kerberos")
public class ClusterKerberosController {

    @Autowired
    private ClusterKerberosService kerberosService;

    /**
     * download keytab
     */
    @GetMapping("/downloadKeytab")
    public void downloadKeytab(Integer clusterId, String principal, String keytabName, String hostname,
                               HttpServletResponse response) throws IOException {
        kerberosService.downloadKeytab(clusterId, principal, keytabName, hostname, response);
    }

    /**
     * upload keytab
     */
    @PostMapping(value = "/uploadKeytab", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFile(@RequestParam(value = "file") MultipartFile file, String hostname,
                           String keytabFileName) throws IOException {
        kerberosService.uploadKeytab(file, hostname, keytabFileName);
    }

}
