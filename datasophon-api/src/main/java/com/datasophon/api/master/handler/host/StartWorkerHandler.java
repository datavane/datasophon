package com.datasophon.api.master.handler.host;

import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.jcraft.jsch.Session;

public class StartWorkerHandler extends HostHandler{

    @Override
    public ExecResult handlerRequest(HostInfo hostInfo, Session session) throws Exception {
        return null;
    }
}
