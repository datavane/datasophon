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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.datasophon.dao.enums.NeedRestart;
import com.datasophon.dao.enums.RoleType;
import com.datasophon.dao.enums.ServiceRoleState;
import lombok.Data;


@TableName("t_ddh_cluster_service_role_instance")
@Data
public class ClusterServiceRoleInstanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 服务角色名称
     */
    private String serviceRoleName;
    /**
     * 主机
     */
    private String hostname;
    /**
     * 服务角色状态 1:正在运行2：存在告警3：存在异常4：需要重启
     */
    private ServiceRoleState serviceRoleState;

    @TableField(exist = false)
    private Integer serviceRoleStateCode;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 服务id
     */
    private Integer serviceId;
    /**
     * 角色类型 1:master2:worker3:client
     */
    private RoleType roleType;
    /**
     * 集群id
     */
    private Integer clusterId;
    /**
     * 服务名称
     */
    private String serviceName;

    private Integer roleGroupId;

    private NeedRestart needRestart;

    @TableField(exist = false)
    private String roleGroupName;

}
