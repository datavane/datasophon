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

package com.datasophon.common.utils;

import java.io.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressUtils {

    private static final Logger logger = LoggerFactory.getLogger(CompressUtils.class);

    public static void main(String[] args) throws IOException {
        decompressTarGz("D:\\DDP\\apache-druid-0.20.2-bin.tar.gz", "D:\\360downloads");
    }

    public static Boolean decompressTarGz(String sourceTarGzFile, String targetDir) {
        logger.info("use tar -zxvf to decompress");
        ArrayList<String> command = new ArrayList<>();
        command.add("tar");
        command.add("-zxvf");
        command.add(sourceTarGzFile);
        command.add("-C");
        command.add(targetDir);
        ExecResult execResult = ShellUtils.execWithStatus(targetDir, command, 120);
        return execResult.getExecResult();
    }

}
