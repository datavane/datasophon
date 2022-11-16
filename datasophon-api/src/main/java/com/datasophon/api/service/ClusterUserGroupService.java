package com.datasophon.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datasophon.dao.entity.ClusterUserGroup;



public interface ClusterUserGroupService extends IService<ClusterUserGroup> {


    Integer countGroupUserNum(Integer id);

    void deleteByUser(Integer id);
}

