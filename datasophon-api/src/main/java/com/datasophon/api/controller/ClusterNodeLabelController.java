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

import com.datasophon.api.service.ClusterNodeLabelService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterNodeLabelEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "集群节点")
@RestController
@RequestMapping("cluster/node/label")
public class ClusterNodeLabelController {

    @Autowired
    private ClusterNodeLabelService nodeLabelService;

    /**
     * save node label
     */
    @ApiOperation(value = "节点标签列表")
    @PostMapping("/list")
    public Result list(Integer clusterId) {
        List<ClusterNodeLabelEntity> list = nodeLabelService.queryClusterNodeLabel(clusterId);
        return Result.success(list);
    }

    /**
     * save node label
     */
    @ApiOperation(value = "保存节点标签")
    @PostMapping("/save")
    public Result save(Integer clusterId, String nodeLabel) {
        return nodeLabelService.saveNodeLabel(clusterId, nodeLabel);
    }

    /**
     * delete node label
     */
    @ApiOperation(value = "删除节点标签")
    @PostMapping("/delete")
    public Result delete(Integer nodeLabelId) {
        return nodeLabelService.deleteNodeLabel(nodeLabelId);
    }

    /**
     * assign node label
     */
    @ApiOperation(value = "分配")
    @PostMapping("/assign")
    public Result assign(Integer nodeLabelId, String hostIds) {
        return nodeLabelService.assignNodeLabel(nodeLabelId, hostIds);
    }
}
