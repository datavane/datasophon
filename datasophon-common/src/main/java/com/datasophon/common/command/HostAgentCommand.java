package com.datasophon.common.command;

import com.datasophon.common.model.HostInfo;
import lombok.Data;

@Data
public class HostAgentCommand {

    private HostInfo hostInfo;

    private Integer clusterId;

    private String commandType;


    public HostAgentCommand(HostInfo hostInfo, Integer clusterId, String commandType) {
        this.hostInfo = hostInfo;
        this.clusterId = clusterId;
        this.commandType = commandType;
    }
}
