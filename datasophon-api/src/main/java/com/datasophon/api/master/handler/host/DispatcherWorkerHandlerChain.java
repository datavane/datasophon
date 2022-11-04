package com.datasophon.api.master.handler.host;

import com.datasophon.common.model.HostInfo;
import com.jcraft.jsch.Session;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DispatcherWorkerHandlerChain {

    private List<DispatcherWorkerHandler> handlers = new ArrayList<>();

    public void addHandler(DispatcherWorkerHandler handler) {
        this.handlers.add(handler);
    }

    public void handle(Session session, HostInfo hostInfo) throws UnknownHostException {
        for (DispatcherWorkerHandler handler : handlers) {
            boolean handled = handler.handle(session, hostInfo);
            if (!handled) {
                break;
            }
        }
    }
}
