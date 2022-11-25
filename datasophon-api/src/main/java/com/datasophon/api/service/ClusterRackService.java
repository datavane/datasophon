package com.datasophon.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterRack;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author dygao2
 * @email dygao2@datasophon.com
 * @date 2022-11-25 11:31:59
 */
public interface ClusterRackService extends IService<ClusterRack> {


    List<ClusterRack> queryClusterRack(Integer clusterId);

    void saveRack(Integer clusterId, String rack);

    Result deleteRack(Integer rackId);
}

