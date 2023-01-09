package com.datasophon.api.master.alert.service;

import com.datasophon.api.master.handler.host.DispatcherWorkerHandler;
import com.datasophon.common.model.HostInfo;
import org.apache.sshd.client.session.ClientSession;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServiceCheckChain {
    private List<ServiceCheckHandler> handlers = new ArrayList<>();

    public void addHandler(ServiceCheckHandler handler) {
        this.handlers.add(handler);
    }

    public void handle(ClientSession session, HostInfo hostInfo)   {
        for (ServiceCheckHandler handler : handlers) {
            handler.handle();
        }
    }
}
