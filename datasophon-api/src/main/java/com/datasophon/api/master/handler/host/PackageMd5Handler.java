package com.datasophon.api.master.handler.host;

import cn.hutool.core.io.FileUtil;
import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Objects;

public class PackageMd5Handler extends HostHandler{

    private static final Logger logger = LoggerFactory.getLogger(PackageMd5Handler.class);
    @Override
    public ExecResult handlerRequest(HostInfo hostInfo, Session session) throws Exception {
        String checkWorkerMd5Result = JSchUtils.execCmdWithResult(session, Constants.CHECK_WORKER_MD5_CMD).trim();
        String md5 = FileUtil.readString(Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH + Constants.WORKER_PACKAGE_NAME + ".md5", Charset.defaultCharset()).trim();
        logger.info("worker package md5 value is : {}",md5);
        if (md5.equals(checkWorkerMd5Result) && Objects.nonNull(getNext())) {
            return getNext().handlerRequest(hostInfo,session);
        }else {
            logger.error("worker package md5 check failed");
            hostInfo.setErrMsg("worker package md5 check failed");
            CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
        }
        return null;
    }
}
