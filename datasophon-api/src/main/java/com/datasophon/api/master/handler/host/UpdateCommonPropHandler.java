package com.datasophon.api.master.handler.host;

import com.datasophon.common.model.HostInfo;
import com.datasophon.common.utils.ExecResult;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCommonPropHandler extends HostHandler{
    private static final Logger logger = LoggerFactory.getLogger(UpdateCommonPropHandler.class);

    @Override
    public ExecResult handlerRequest(HostInfo hostInfo, Session session) throws Exception {
        ExecResult execResult = new ExecResult();
//        String hostName = InetAddress.getLocalHost().getHostName();
//        String updateCommonPropertiesResult = JSchUtils.execCmdWithResult(session, Constants.UPDATE_COMMON_CMD + hostName + Constants.SPACE + configBean.getServerPort() + Constants.SPACE + command.getClusterFrame() + Constants.SPACE + command.getClusterId());
//        if (StringUtils.isNotBlank(updateCommonPropertiesResult)) {
//            //更新进度为60%
//            execResult.setExecResult(true);
//            CommonUtils.updateProgress(60, hostInfo);
//            if(Objects.nonNull(getNext())){
//                return handlerRequest(hostInfo,session);
//            }
//
//        }else{
//            logger.error("common.properties update failed");
//            hostInfo.setErrMsg("common.properties update failed");
//            CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
//        }

        return execResult;
    }
}
