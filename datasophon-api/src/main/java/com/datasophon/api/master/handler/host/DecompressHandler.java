package com.datasophon.api.master.handler.host;

import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DecompressHandler extends HostHandler{

    private static final Logger logger = LoggerFactory.getLogger(DecompressHandler.class);
    @Override
    public ExecResult handlerRequest(HostInfo hostInfo, Session session) throws Exception {

        String tarResult = JSchUtils.execCmdWithResult(session, Constants.UNZIP_DDH_WORKER_CMD);
        if (StringUtils.isNotBlank(tarResult) && Objects.nonNull(getNext())) {
            CommonUtils.updateProgress(50, hostInfo);
            return getNext().handlerRequest(hostInfo,session);
        }
        logger.error("tar -zxvf ddh-worker.tar.gz failed");
        hostInfo.setErrMsg("tar -zxvf ddh-worker.tar.gz failed");
        CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
        return null;
    }
}
