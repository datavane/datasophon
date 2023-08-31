package com.datasophon.api.check;

import com.datasophon.api.enums.Status;
import com.datasophon.api.exceptions.ServiceException;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.model.ServiceRoleHostMapping;
import com.datasophon.common.utils.CollectionUtils;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class CheckDorisRule extends CheckRule{
    private static final List<String> MUST_AT_NOT_SAME_NODE_DORIS =
            Arrays.asList("DorisFE","DorisFEObserver");

    @Autowired
    private ClusterServiceRoleInstanceService roleInstanceService;

    @Override
    public void checkeNodeRule(Integer clusterId, List<ServiceRoleHostMapping> list) {
        Set<String> hostnameSet =
                list.stream()
                        .filter(s -> MUST_AT_NOT_SAME_NODE_DORIS.contains(s.getServiceRole()))
                        .map(ServiceRoleHostMapping::getHosts)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(hostnameSet)) {
            return;
        }


        Map<String, String> roleHostMap = roleInstanceService.lambdaQuery()
                .eq(ClusterServiceRoleInstanceEntity::getClusterId, clusterId)
                .in(
                        ClusterServiceRoleInstanceEntity::getServiceName,
                        MUST_AT_NOT_SAME_NODE_DORIS)
                .list().stream()
                .collect(Collectors.toMap(ClusterServiceRoleInstanceEntity::getHostname,
                        ClusterServiceRoleInstanceEntity::getServiceRoleName));

        if (roleHostMap.size() < 2) {
            throw new ServiceException(Status.DORIS_FE_OBSERVER_NUST_IN_DIFFERENCE_NODE.getMsg());
        }
    }
}
