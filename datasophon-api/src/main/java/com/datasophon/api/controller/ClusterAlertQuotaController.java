package com.datasophon.api.controller;

import java.io.IOException;
import java.util.*;

import com.datasophon.api.service.FrameServiceService;
import com.datasophon.common.model.AlertItem;
import com.datasophon.common.model.Generators;
import com.datasophon.common.utils.FreemakerUtils;
import com.datasophon.dao.entity.FrameServiceEntity;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterAlertQuota;
import com.datasophon.api.service.ClusterAlertQuotaService;

/**
 * 集群告警指标表 
 *
 * @author gaodayu
 * @email gaodayu2022@163.com
 * @date 2022-06-24 15:10:41
 */
@RestController
@RequestMapping("cluster/alert/quota")
public class ClusterAlertQuotaController {
    @Autowired
    private ClusterAlertQuotaService clusterAlertQuotaService;

    @Autowired
    private FrameServiceService service;


    /**
     * 列表
     */
    @RequestMapping("/generateAlertYml")
    public Result list(Integer clusterId) throws IOException, TemplateException {
        List<ClusterAlertQuota> list = clusterAlertQuotaService.list();
        List<FrameServiceEntity> serviceList = service.list();
        for (FrameServiceEntity serviceEntity : serviceList) {
            Generators generators = new Generators();
            generators.setFilename(serviceEntity.getServiceName().toLowerCase()+".yml");
            generators.setConfigFormat("prometheus");
            generators.setOutputDirectory("D:\\360downloads\\test\\");
            ArrayList<AlertItem> alertItems = new ArrayList<>();
            for (ClusterAlertQuota clusterAlertQuota : list) {
                if(clusterAlertQuota.getServiceCategory().equals(serviceEntity.getServiceName()) && StringUtils.isNotBlank(clusterAlertQuota.getServiceRoleName())){
                    AlertItem alertItem = new AlertItem();
                    alertItem.setAlertName(clusterAlertQuota.getAlertQuotaName());
                    alertItem.setAlertExpr(clusterAlertQuota.getAlertExpr()+" "+ clusterAlertQuota.getCompareMethod()+" "+clusterAlertQuota.getAlertThreshold());
                    alertItem.setClusterId(clusterId);
                    alertItem.setServiceRoleName(clusterAlertQuota.getServiceRoleName());
                    alertItem.setAlertLevel(clusterAlertQuota.getAlertLevel().getDesc());
                    alertItem.setAlertAdvice(clusterAlertQuota.getAlertAdvice());
                    alertItem.setTriggerDuration(clusterAlertQuota.getTriggerDuration());
                    alertItems.add(alertItem);
                }
            }
            if(alertItems.size() > 0){
                FreemakerUtils.generatePromAlertFile(generators,alertItems,serviceEntity.getServiceName());
            }
        }

        return Result.success();
    }


    /**
     * 信息
     */
    @RequestMapping("/list")
    public Result info(Integer clusterId,Integer alertGroupId,String quotaName,Integer page,Integer pageSize){
        return clusterAlertQuotaService.getAlertQuotaList(clusterId,alertGroupId,quotaName,page,pageSize);
    }

    /**
     * 启用
     */
    @RequestMapping("/start")
    public Result start(Integer clusterId,String alertQuotaIds){
        return clusterAlertQuotaService.start(clusterId,alertQuotaIds);
    }

    /**
     * 停用
     */
    @RequestMapping("/stop")
    public Result stop(Integer clusterId,String alertQuotaIds){
        return clusterAlertQuotaService.stop(clusterId,alertQuotaIds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody ClusterAlertQuota clusterAlertQuota){


        clusterAlertQuotaService.saveAlertQuota(clusterAlertQuota);
        return Result.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public Result update(@RequestBody ClusterAlertQuota clusterAlertQuota){

        clusterAlertQuotaService.updateById(clusterAlertQuota);
        
        return Result.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        clusterAlertQuotaService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
