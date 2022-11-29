package com.datasophon.api.master.handler.host;

import com.datasophon.api.utils.MinaUtils;
import com.datasophon.common.model.HostInfo;

import java.net.UnknownHostException;

public interface DispatcherWorkerHandler {
    boolean handle(MinaUtils minaUtils, HostInfo hostInfo) throws UnknownHostException;
}
