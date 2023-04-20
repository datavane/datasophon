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

package com.datasophon.dao.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("t_ddh_cluster_service_role_instance_config")
public class ClusterServiceRoleInstanceConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主机
     */
    @TableId
    private Integer id;
    /**
     * 服务角色实例id
     */
    private Integer service_role_instance_id;
    /**
     * 创建时间
     */
    private Date create_time;
    /**
     * 配置json
     */
    private String config_json;
    /**
     * 更新时间
     */
    private Date update_time;
    /**
     * 配置json md5
     */
    private String config_json_md5;
    /**
     * 配置json版本
     */
    private String config_json_version;

}
