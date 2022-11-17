package com.datasophon.api.controller;

import com.datasophon.api.service.ClusterNodeLabelService;
import com.datasophon.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cluster/node/label")
public class ClusterNodeLabelController {

    @Autowired
    private ClusterNodeLabelService nodeLabelService;

    /**
     * save node label
     */
    @RequestMapping("/save")
    public Result save(Integer clusterId, String nodeLabel) {
        return nodeLabelService.saveNodeLabel(clusterId, nodeLabel);
    }

    /**
     * delete node label
     */
    @RequestMapping("/delete")
    public Result delete(Integer nodeLabelId) {
        return nodeLabelService.deleteNodeLabel(nodeLabelId);
    }

    /**
     * assign node label
     */
    @RequestMapping("/assign")
    public Result assign(Integer nodeLabelId, String hostIds) {
        return nodeLabelService.assignNodeLabel(nodeLabelId, hostIds);
    }
}
