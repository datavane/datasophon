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
 *
 */
package com.datasophon.api.enums;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 *  status enum
 */
public enum Status {

    SUCCESS(200, "success", "成功"),

    INTERNAL_SERVER_ERROR_ARGS(10000, "Internal Server Error: {0}", "服务端异常: {0}"),

    USER_NAME_EXIST(10003, "user name already exists", "用户名已存在"),
    USER_NAME_NULL(10004,"user name is null", "用户名不能为空"),
    USER_NOT_EXIST(10010, "user {0} not exists", "用户[{0}]不存在"),
    USER_NAME_PASSWD_ERROR(10013,"user name or password error", "用户名或密码错误"),
    LOGIN_SESSION_FAILED(10014,"create session failed!", "创建session失败"),
    REQUEST_PARAMS_NOT_VALID_ERROR(10101, "request parameter {0} is not valid", "请求参数[{0}]无效"),
    CREATE_USER_ERROR(10090,"create user error", "创建用户错误"),
    QUERY_USER_LIST_PAGING_ERROR(10091,"query user list paging error", "分页查询用户列表错误"),
    UPDATE_USER_ERROR(10092,"update user error", "更新用户错误"),
    LOGIN_SUCCESS(10042,"login success", "登录成功"),
    IP_IS_EMPTY(10125,"ip is empty", "IP地址不能为空"),
    DELETE_USER_BY_ID_ERROR(10093,"delete user by id error", "删除用户错误"),

    START_CHECK_HOST(10000,"start check host","开始主机校验"),
    CHECK_HOST_SUCCESS(10001,"check host success","主机校验成功"),
    NEED_JAVA_ENVIRONMENT(10002,"need java environment","缺少Java环境"),
    CONNECTION_FAILED(10003,"connection failed","主机连接失败"),
    NEED_HOSTNAME(10004,"need hostname","无法获取主机名"),
    CAN_NOT_GET_IP(10005,"can not get ip","无法获取ip地址"),
    INSTALL_SERVICE(10006,"Install Service ","安装服务"),

    CLUSTER_CODE_EXISTS(10007,"cluster code exists","集群编码已存在"),
    USER_NO_OPERATION_PERM(30001, "user has no operation privilege", "当前用户没有操作权限"),
    ;



    private final int code;
    private final String enMsg;
    private final String zhMsg;

    private Status(int code, String enMsg, String zhMsg) {
        this.code = code;
        this.enMsg = enMsg;
        this.zhMsg = zhMsg;
    }

    public int getCode() {
        return this.code;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("code",this.code);
        json.put("msg",getMsg());
        return json;
    }

    public String getMsg() {
        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage())) {
            return this.zhMsg;
        } else {
            return this.zhMsg;
        }
    }
}
