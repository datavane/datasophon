package com.datasophon.api.master.handler.host;

import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.jcraft.jsch.Session;
import lombok.Data;

@Data
public abstract class HostHandler {

    private HostHandler next;
    public abstract ExecResult handlerRequest(HostInfo hostInfo, Session session) throws Exception;
}
