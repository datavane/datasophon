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

import com.datasophon.common.Constants;
import com.datasophon.common.model.PromMetricInfo;
import com.datasophon.common.model.PromResponceInfo;
import com.datasophon.common.model.PromResultInfo;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.hutool.http.HttpUtil;

/**
 * @Title: prometheus工具类
 * @Description:
 */
public class PromInfoUtils {

    private static final Logger log = LoggerFactory.getLogger(PromInfoUtils.class);

    public static List<PromResultInfo> getPrometheusMetrics(String promURL, String promQL) {

        // log.info("请求地址：{}，请求QL：{}", promURL, promQL);
        Map param = new HashMap<String, String>();
        param.put(Constants.QUERY, promQL);
        String http = null;
        try {
            http = HttpUtil.get(promURL, param, 10000);
        } catch (Exception e) {
            log.error("请求地址：{}，请求QL：{}，异常信息：{}", promURL, promQL, e);
        }
        PromResponceInfo responceInfo = JSON.parseObject(http, PromResponceInfo.class);
        // log.info("请求地址：{}，请求QL：{}，返回信息：{}", promURL, promQL, responceInfo);
        if (Objects.isNull(responceInfo)) {
            return null;
        }
        String status = responceInfo.getStatus();
        if (StringUtils.isBlank(status)
                || !Constants.SUCCESS.equals(status)) {
            return null;
        }
        List<PromResultInfo> result = responceInfo.getData().getResult();
        return result;
    }

    public static String getSinglePrometheusMetric(String promURL, String promQL) {

        // log.info("请求地址：{}，请求QL：{}", promURL, promQL);
        Map param = new HashMap<String, String>();
        param.put(Constants.QUERY, promQL);
        String http = null;
        try {
            http = HttpUtil.get(promURL, param, 10000);
        } catch (Exception e) {
            log.error("请求地址：{}，请求QL：{}，异常信息：{}", promURL, promQL, e.getMessage());
        }
        PromResponceInfo responceInfo = JSON.parseObject(http, PromResponceInfo.class);
        // log.info("请求地址：{}，请求QL：{}，返回信息：{}", promURL, promQL, responceInfo);
        if (Objects.isNull(responceInfo)) {
            return null;
        }
        String status = responceInfo.getStatus();
        if (StringUtils.isBlank(status)
                || !Constants.SUCCESS.equals(status)) {
            log.error("请求地址：{}，请求QL：{}，异常信息：{}", promURL, promQL, "status is failed");
            return null;
        }
        List<PromResultInfo> result = responceInfo.getData().getResult();
        if (result.size() > 0) {
            return result.get(0).getValue()[1];
        }
        return null;
    }

    public static void main(String[] args) {
        // List<PromResultInfo> windowsOsInfo = getWindowsInfo("http://10.0.50.225:9090/api/v1/query",
        // PromConstants.WINDOWS_OS_INFO);

        List<PromResultInfo> hadoop_nameNode_threads = getPrometheusMetrics("http://172.31.96.16:9090/api/v1/query",
                "up{job=\"hdfs\",instance=\"172.31.96.16:27001\"}");
        for (PromResultInfo hadoop_nameNode_thread : hadoop_nameNode_threads) {
            PromMetricInfo metric = hadoop_nameNode_thread.getMetric();
            log.info(metric.get__name__() + ":" + hadoop_nameNode_thread.getValue()[1]);
        }
    }

}
