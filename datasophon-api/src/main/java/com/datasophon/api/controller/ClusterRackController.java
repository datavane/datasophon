package com.datasophon.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterRack;
import com.datasophon.api.service.ClusterRackService;

/**
 * 
 *
 * @author dygao2
 * @email dygao2@datasophon.com
 * @date 2022-11-25 11:31:59
 */
@RestController
@RequestMapping("cluster/rack")
public class ClusterRackController {
    @Autowired
    private ClusterRackService clusterRackService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(Integer clusterId){
        List<ClusterRack> list = clusterRackService.queryClusterRack(clusterId);
        return Result.success(list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterRack clusterRack = clusterRackService.getById(id);

        return Result.success().put("clusterRack", clusterRack);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(Integer clusterId , String rack){
        clusterRackService.saveRack(clusterId,rack);
        return Result.success();
    }



    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(Integer clusterId ,Integer rackId){
        return clusterRackService.deleteRack(rackId);
    }

}
