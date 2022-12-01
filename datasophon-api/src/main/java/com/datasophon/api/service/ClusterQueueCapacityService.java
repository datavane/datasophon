package com.datasophon.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterQueueCapacity;

import java.util.Map;

/**
 * 
 *
 * @author dygao2
 * @email dygao2@datasophon.com
 * @date 2022-11-25 14:30:11
 */
public interface ClusterQueueCapacityService extends IService<ClusterQueueCapacity> {


    Result refreshToYarn(Integer clusterId) throws Exception;
}

