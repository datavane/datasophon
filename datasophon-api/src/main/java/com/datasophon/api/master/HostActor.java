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
    public void onReceive(Object message) throws Throwable, Throwable {

        if (message instanceof StartWorkerMessage) {
            StartWorkerMessage host = (StartWorkerMessage) message;
            logger.info("receive message when worker first start :{}", host.getHostname());
            ClusterHostService clusterHostService = (ClusterHostService) SpringTool.getBean("clusterHostService");
            ClusterInfoService clusterInfoService = (ClusterInfoService) SpringTool.getBean("clusterInfoService");

            //启动该节点上停止的服务
            ClusterServiceRoleInstanceService roleInstanceService = SpringTool.getApplicationContext().getBean(ClusterServiceRoleInstanceService.class);
            List<ClusterServiceRoleInstanceEntity> list = roleInstanceService.list(new QueryWrapper<ClusterServiceRoleInstanceEntity>()
                    .eq(Constants.CLUSTER_ID, host.getClusterId())
                    .eq(Constants.HOSTNAME, host.getHostname())
                    .eq(Constants.SERVICE_ROLE_STATE, ServiceRoleState.STOP));
            if(Objects.nonNull(list) && list.size() > 0){
                for (ClusterServiceRoleInstanceEntity roleInstanceEntity : list) {
                    logger.info("start {} in host {}",roleInstanceEntity.getServiceRoleName(),host.getHostname());
                    ArrayList<String> ids = new ArrayList<>();
                    ids.add(roleInstanceEntity.getId().toString());
                    ClusterServiceCommandService serviceCommandService = SpringTool.getApplicationContext().getBean(ClusterServiceCommandService.class);
                    serviceCommandService.generateServiceRoleCommand(host.getClusterId(), CommandType.START_SERVICE, roleInstanceEntity.getServiceId(), ids);
                }

            }

            //判断当前主机是否已受管
            ClusterHostEntity hostEntity = clusterHostService.getClusterHostByHostname(host.getHostname());
            ClusterInfoEntity cluster = clusterInfoService.getById(host.getClusterId());
            //主机管理安装进度设为100%
            logger.info("host install set to 100%");
            if (CacheUtils.constainsKey(cluster.getClusterCode() + Constants.HOST_MAP)) {
                HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(cluster.getClusterCode() + Constants.HOST_MAP);
                HostInfo hostInfo = map.get(host.getHostname());
                if(Objects.nonNull(hostInfo)){
                    hostInfo.setProgress(100);
                    hostInfo.setInstallState(InstallState.SUCCESS);
                    hostInfo.setInstallStateCode(InstallState.SUCCESS.getValue());
                    hostInfo.setManaged(true);
                }
            }
            if (ObjectUtil.isNull(hostEntity)) {
                //主机信息持久化到数据库
                ProcessUtils.saveHostInstallInfo(host,cluster.getClusterCode(), clusterHostService);
                logger.info("host install save to database");
            }else{
                hostEntity.setCpuArchitecture(host.getCpuArchitecture());
                hostEntity.setManaged(MANAGED.YES);
                clusterHostService.updateById(hostEntity);
            }
            //添加主机监控到prometheus
            ActorRef prometheusActor = (ActorRef) CacheUtils.get("prometheusActor");
            GenerateHostPrometheusConfig prometheusConfigCommand = new GenerateHostPrometheusConfig();
            prometheusConfigCommand.setClusterId(cluster.getId());
            prometheusActor.tell(prometheusConfigCommand, getSelf());
        } else if (message instanceof HostCheckCommand) {
            HostCheckCommand hostCheckCommand = (HostCheckCommand) message;
            HostInfo hostInfo = hostCheckCommand.getHostInfo();
            logger.info("start host check:{}", hostInfo.getHostname());
            Session session = JSchUtils.getSession(hostInfo.getHostname(), hostInfo.getSshPort(), hostInfo.getSshUser(),
                    Constants.SLASH+ hostInfo.getSshUser() + Constants.ID_RSA);
            if (ObjectUtil.isNotNull(session)) {
                hostInfo.setCheckResult(new CheckResult(Status.CHECK_HOST_SUCCESS.getCode(), Status.CHECK_HOST_SUCCESS.getMsg()));
            } else {
                hostInfo.setCheckResult(new CheckResult(Status.CONNECTION_FAILED.getCode(), Status.CONNECTION_FAILED.getMsg()));
            }
            HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(hostInfo.getClusterCode() + Constants.HOST_MAP);
            map.put(hostInfo.getHostname(), hostInfo);
            logger.info("end host check:{}", hostInfo.getHostname());
            session.disconnect();
        } else if (message instanceof DispatcherHostAgentCommand) {
            ConfigBean configBean = SpringTool.getApplicationContext().getBean(ConfigBean.class);
            DispatcherHostAgentCommand command = (DispatcherHostAgentCommand) message;
            HostInfo hostInfo = command.getHostInfo();
            String hostname = hostInfo.getHostname();
            logger.info("start dispatcher host agent :{}", hostInfo.getHostname());
            hostInfo.setMessage("开始分发主机管理agent安装包");
            Session session = JSchUtils.getSession(hostInfo.getHostname(), hostInfo.getSshPort(), hostInfo.getSshUser(),
                    Constants.SLASH + hostInfo.getSshUser() + Constants.ID_RSA);
            boolean uploadFile = JSchUtils.uploadFile(session, Constants.INSTALL_WORKER_PATH, Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH + Constants.WORKER_PACKAGE_NAME);
            //校验包完整性
            if (uploadFile) {
                hostInfo.setMessage("分发成功，开始校验md5");
                hostInfo.setProgress(25);
                String checkWorkerMd5Result = JSchUtils.execCmdWithResult(session, Constants.CHECK_WORKER_MD5_CMD).trim();
                String md5 = FileUtil.readString(Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH + Constants.WORKER_PACKAGE_NAME + ".md5", Charset.defaultCharset()).trim();
                logger.info("{} worker package md5 value is : {}",hostname,md5);
                if (!md5.equals(checkWorkerMd5Result)) {
                    logger.error("worker package md5 check failed");
                    hostInfo.setErrMsg("worker package md5 check failed");
                    hostInfo.setMessage("md5校验失败");
                    CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
                } else {
                    //解压安装包，执行启动脚本
                    hostInfo.setMessage("md5校验成功，开始解压安装包");
                    String tarResult = JSchUtils.execCmdWithResult(session, Constants.UNZIP_DDH_WORKER_CMD);
                    if (StringUtils.isBlank(tarResult)) {
                        logger.error("tar -zxvf ddh-worker.tar.gz failed");
                        hostInfo.setErrMsg("tar -zxvf ddh-worker.tar.gz failed");
                        hostInfo.setMessage("解压安装包失败");
                        CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
                    } else {
                        //更新common.properties
//                        CommonUtils.updateProgress(50, hostInfo);
                        hostInfo.setProgress(50);
                        hostInfo.setMessage("安装包解压成功，开始修改配置文件");
                        String hostName = InetAddress.getLocalHost().getHostName();
                        String updateCommonPropertiesResult = JSchUtils.execCmdWithResult(session, Constants.UPDATE_COMMON_CMD + hostName + Constants.SPACE + configBean.getServerPort() + Constants.SPACE + command.getClusterFrame() + Constants.SPACE + command.getClusterId());
                        if (StringUtils.isBlank(updateCommonPropertiesResult)) {
                            logger.error("common.properties update failed");
                            hostInfo.setErrMsg("common.properties update failed");
                            hostInfo.setMessage("配置文件修改失败");
                            CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
                        } else {
                            //更新进度为60%
//                            CommonUtils.updateProgress(60, hostInfo);
                            hostInfo.setProgress(60);
                            String arch = JSchUtils.execCmdWithResult(session, "arch");
                            String testResult = JSchUtils.execCmdWithResult(session, "test -d /usr/local/jdk1.8.0_333");
                            boolean exists = true;
                            if(StringUtils.isNotBlank(testResult) && "failed".equals(testResult)){
                                exists = false;
                            }
                            if("x86_64".equals(arch) ){
//                                hostInfo.setCpuArchitecture("x86_64");
//                                JSchUtils.execCmdWithResult(session, Constants.START_X86_NODE_CMD);
                                if(!exists){
                                    hostInfo.setMessage("开始安装jdk");
                                    JSchUtils.uploadFile(session, "/usr/local",Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH +Constants.X86JDK);
                                    JSchUtils.execCmdWithResult(session, "tar -zxvf /usr/local/jdk-8u333-linux-x64.tar.gz -C /usr/local/");
                                }
                            }
                            if("aarch64".equals(arch)){
//                                hostInfo.setCpuArchitecture("arm64");
//                                JSchUtils.execCmdWithResult(session, Constants.START_ARM_NODE_CMD);
                                if(!exists){
                                    hostInfo.setMessage("开始安装jdk");
                                    JSchUtils.uploadFile(session, "/usr/local",Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH +Constants.ARMJDK);
                                    JSchUtils.execCmdWithResult(session, "tar -zxvf /usr/local/jdk-8u333-linux-aarch64.tar.gz -C /usr/local/");
                                }
                            }
                            //设置开机自动启动
                            JSchUtils.execCmdWithResult(session, "\\cp /opt/datasophon/ddh-worker-1.0.0/ddh-worker/script/ddh-worker /etc/rc.d/init.d/");
                            JSchUtils.execCmdWithResult(session, "chmod +x /etc/rc.d/init.d/ddh-worker");
                            JSchUtils.execCmdWithResult(session, "chkconfig --add ddh-worker");
//                            JSchUtils.execCmdWithResult(session,"systemctl enable ddh-worker");
//                            JSchUtils.execCmdWithResult(session,"systemctl daemon-reload");
                            JSchUtils.execCmdWithResult(session,"\\cp /opt/datasophon/ddh-worker-1.0.0/ddh-worker/script/profile /etc/");
                            JSchUtils.execCmdWithResult(session,"source /etc/profile");
                            hostInfo.setMessage("启动主机管理agent");
                            JSchUtils.execCmdWithResult(session, Constants.START_DDH_WORKER_CMD);

//                            CommonUtils.updateProgress(75, hostInfo);
                            hostInfo.setProgress(75);
                            hostInfo.setCreateTime(new Date());
                        }

                    }
                }

            } else {
                hostInfo.setMessage("分发主机管理agent安装包失败");
                hostInfo.setErrMsg("dispatcher host agent to " + hostInfo.getHostname() + " failed");
                CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
            }
            logger.info("end dispatcher host agent :{}", hostInfo.getHostname());
            session.disconnect();
        } else {
            unhandled(message);
        }

    }
}
