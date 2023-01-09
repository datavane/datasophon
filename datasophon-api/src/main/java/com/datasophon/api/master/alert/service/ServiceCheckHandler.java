package com.datasophon.api.master.alert.service;

import com.datasophon.common.model.HostInfo;
import org.apache.sshd.client.session.ClientSession;

import java.net.UnknownHostException;

public interface ServiceCheckHandler {
    void handle() ;
}
