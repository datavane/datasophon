package com.datasophon.api.check;

import com.datasophon.api.enums.Status;
import com.datasophon.api.exceptions.ServiceException;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.common.model.ServiceRoleHostMapping;
import com.datasophon.common.utils.CollectionUtils;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckBasicServiceRule extends CheckRule{
    private static final List<String> MUST_AT_SAME_NODE_BASIC_SERVICE =
            Arrays.asList("Grafana", "AlertManager", "Prometheus");


    private ClusterServiceRoleInstanceService roleInstanceService;

    public CheckBasicServiceRule(ClusterServiceRoleInstanceService roleInstanceService) {
        this.roleInstanceService = roleInstanceService;
    }

    @Override
    public void checkeNodeRule(Integer clusterId, List<ServiceRoleHostMapping> list) {
        Set<String> hostnameSet =
                list.stream()
                        .filter(s -> MUST_AT_SAME_NODE_BASIC_SERVICE.contains(s.getServiceRole()))
                        .map(ServiceRoleHostMapping::getHosts)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(hostnameSet)) {
            return;
        }

        Set<String> installedHostnameSet =
                roleInstanceService.lambdaQuery()
                        .eq(ClusterServiceRoleInstanceEntity::getClusterId, clusterId)
                        .in(
                                ClusterServiceRoleInstanceEntity::getServiceName,
                                MUST_AT_SAME_NODE_BASIC_SERVICE)
                        .list().stream()
                        .map(ClusterServiceRoleInstanceEntity::getHostname)
                        .collect(Collectors.toSet());
        hostnameSet.addAll(installedHostnameSet);

        if (hostnameSet.size() > 1) {
            throw new ServiceException(Status.BASIC_SERVICE_SELECT_MOST_ONE_HOST.getMsg());
        }
    }
}
