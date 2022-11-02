package com.datasophon.api.master.handler.host;

import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.jcraft.jsch.Session;

import java.util.Objects;

public class UploadPackageHandler extends HostHandler{
    @Override
    public ExecResult handlerRequest(HostInfo hostInfo,Session session) throws Exception {
//                    "D:\\360Downloads\\id_rsa");
        boolean uploadFile = JSchUtils.uploadFile(session, Constants.INSTALL_WORKER_PATH, Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH + Constants.WORKER_PACKAGE_NAME);
        if(uploadFile && Objects.nonNull(getNext())){
            CommonUtils.updateProgress(25, hostInfo);
            return getNext().handlerRequest(hostInfo,session);
        }
        return null;
    }
}
