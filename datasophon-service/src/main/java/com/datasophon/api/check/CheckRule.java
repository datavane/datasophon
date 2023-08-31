package com.datasophon.api.check;

import com.datasophon.common.model.ServiceRoleHostMapping;

import java.util.List;

public abstract class CheckRule {
    public abstract void checkeNodeRule(Integer clusterId, List<ServiceRoleHostMapping> list);
}
