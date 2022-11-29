package com.datasophon.api.master.handler.host;

import com.datasophon.api.utils.MinaUtils;
import com.datasophon.common.model.HostInfo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DispatcherWorkerHandlerChain {

    private List<DispatcherWorkerHandler> handlers = new ArrayList<>();

    public void addHandler(DispatcherWorkerHandler handler) {
        this.handlers.add(handler);
    }

    public void handle(MinaUtils minaUtils, HostInfo hostInfo) throws UnknownHostException {
        for (DispatcherWorkerHandler handler : handlers) {
            boolean handled = handler.handle(minaUtils, hostInfo);
            if (!handled) {
                break;
            }
        }
    }
}
