package com.datasophon.api.master;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datasophon.api.service.ClusterInfoService;
import com.datasophon.api.service.ClusterServiceCommandService;
import com.datasophon.api.configuration.ConfigBean;
import com.datasophon.api.enums.Status;
import com.datasophon.api.service.ClusterServiceRoleInstanceService;
import com.datasophon.api.utils.CommonUtils;
import com.datasophon.common.command.DispatcherHostAgentCommand;
import com.datasophon.common.command.GenerateHostPrometheusConfig;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.model.CheckResult;
import com.datasophon.common.model.HostInfo;
import com.datasophon.api.service.ClusterHostService;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.api.utils.SpringTool;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.command.HostCheckCommand;
import com.datasophon.common.model.StartWorkerMessage;
import com.datasophon.dao.entity.ClusterHostEntity;
import com.datasophon.dao.entity.ClusterInfoEntity;
import com.datasophon.common.enums.InstallState;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import com.datasophon.dao.enums.MANAGED;
import com.datasophon.dao.enums.ServiceRoleState;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.*;

public class HostActor extends UntypedActor {
    private static final Logger logger = LoggerFactory.getLogger(HostActor.class);

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        logger.info("host actor restart because {}", reason.getMessage());
        super.preRestart(reason, message);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof HostCheckCommand) {
            HostCheckCommand hostCheckCommand = (HostCheckCommand) message;
            HostInfo hostInfo = hostCheckCommand.getHostInfo();
            logger.info("start host check:{}", hostInfo.getHostname());
            Session session = JSchUtils.getSession(
                    hostInfo.getHostname(),
                    hostInfo.getSshPort(),
                    hostInfo.getSshUser(),
                    Constants.SLASH + hostInfo.getSshUser() + Constants.ID_RSA);
            if (ObjectUtil.isNotNull(session)) {
                hostInfo.setCheckResult(new CheckResult(Status.CHECK_HOST_SUCCESS.getCode(), Status.CHECK_HOST_SUCCESS.getMsg()));
            } else {
                hostInfo.setCheckResult(new CheckResult(Status.CONNECTION_FAILED.getCode(), Status.CONNECTION_FAILED.getMsg()));
                session.disconnect();
            }
            logger.info("end host check:{}", hostInfo.getHostname());

        } else {
            unhandled(message);
        }
    }
}
