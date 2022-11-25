package com.datasophon.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterQueueCapacity;
import com.datasophon.api.service.ClusterQueueCapacityService;

/**
 * 
 *
 * @author dygao2
 * @email dygao2@datasophon.com
 * @date 2022-11-25 14:30:11
 */
@RestController
@RequestMapping("cluster/queue/capacity")
public class ClusterQueueCapacityController {

    @Autowired
    private ClusterQueueCapacityService clusterQueueCapacityService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(Integer clusterId){

        List<ClusterQueueCapacity> list = clusterQueueCapacityService.list(new QueryWrapper<ClusterQueueCapacity>().eq(Constants.CLUSTER_ID, clusterId));

        return Result.success(list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
        ClusterQueueCapacity clusterQueueCapacity = clusterQueueCapacityService.getById(id);

        return Result.success().put("clusterQueueCapacity", clusterQueueCapacity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody ClusterQueueCapacity clusterQueueCapacity){
        clusterQueueCapacityService.save(clusterQueueCapacity);

        return Result.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody ClusterQueueCapacity clusterQueueCapacity){

        clusterQueueCapacityService.updateById(clusterQueueCapacity);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete( Integer id){
        clusterQueueCapacityService.removeById(id);

        return Result.success();
    }

}
