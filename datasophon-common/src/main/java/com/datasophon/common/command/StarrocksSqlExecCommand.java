package com.datasophon.common.command;

import java.io.Serializable;

import lombok.Data;

@Data
public class StarrocksSqlExecCommand implements Serializable {

    private String opsType;

    private String feMaster;

    private String hostName;
}
