package com.datasophon.worker.strategy;

import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.utils.ExecResult;

public interface ServiceRoleStrategy {
    public ExecResult handler(ServiceRoleOperateCommand command );
}
