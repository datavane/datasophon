package com.datasophon.api.master.handler.host;

import com.datasophon.common.model.HostInfo;
import com.jcraft.jsch.Session;

import java.net.UnknownHostException;

public interface DispatcherWorkerHandler {
    boolean handle(Session session, HostInfo hostInfo) throws UnknownHostException;
}
