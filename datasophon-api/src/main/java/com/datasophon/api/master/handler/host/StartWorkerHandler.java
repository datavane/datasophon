package com.datasophon.api.master.handler.host;

import com.datasophon.api.configuration.ConfigBean;
import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.MinaUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class StartWorkerHandler implements DispatcherWorkerHandler{

    private static final Logger logger = LoggerFactory.getLogger(StartWorkerHandler.class);

    private Integer clusterId;

    private String clusterFrame;

    public StartWorkerHandler(Integer clusterId ,String clusterFrame) {
        this.clusterId = clusterId;
        this.clusterFrame = clusterFrame;
    }

    @Override
    public boolean handle(MinaUtils minaUtils, HostInfo hostInfo) throws UnknownHostException {
        ConfigBean configBean = SpringTool.getApplicationContext().getBean(ConfigBean.class);
        String installPath = Constants.INSTALL_PATH;
        String localHostName = InetAddress.getLocalHost().getHostName();
        String updateCommonPropertiesResult = minaUtils.execCmdWithResult(
                Constants.UPDATE_COMMON_CMD +
                        localHostName +
                        Constants.SPACE +
                        configBean.getServerPort() +
                        Constants.SPACE +
                        this.clusterFrame +
                        Constants.SPACE +
                        this.clusterId);
        if (StringUtils.isBlank(updateCommonPropertiesResult) || "failed".equals(updateCommonPropertiesResult)) {
            logger.error("common.properties update failed");
            hostInfo.setErrMsg("common.properties update failed");
            hostInfo.setMessage("配置文件修改失败");
            CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
        } else {
            //设置开机自动启动
            minaUtils.execCmdWithResult( "\\cp "+installPath+"/datasophon-worker/script/datasophon-worker /etc/rc.d/init.d/");
            minaUtils.execCmdWithResult( "chmod +x /etc/rc.d/init.d/datasophon-worker");
            minaUtils.execCmdWithResult("chkconfig --add datasophon-worker");
            minaUtils.execCmdWithResult("\\cp "+installPath+"/datasophon-worker/script/profile /etc/");
            minaUtils.execCmdWithResult("source /etc/profile");
            hostInfo.setMessage("启动主机管理agent");
            minaUtils.execCmdWithResult( "service datasophon-worker restart");
            hostInfo.setProgress(75);
            hostInfo.setCreateTime(new Date());
        }

        logger.info("end dispatcher host agent :{}", hostInfo.getHostname());
        return true;
    }
}
