package com.datasophon.api.controller;

import com.datasophon.api.security.UserPermission;
import com.datasophon.api.service.InstallService;
import com.datasophon.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 集群部署操作
 */
@RestController
@RequestMapping("host/install")
public class HostInstallController {

    @Autowired
    private InstallService installService;

    /**
     * 获取安装步骤
     */
    @GetMapping("/getInstallStep")
    public Result getInstallStep(Integer type) {
        return installService.getInstallStep(type);
    }
    /**
     * 解析主机列表
     */
    @PostMapping("/analysisHostList")
    @UserPermission
    public Result analysisHostList(Integer clusterId,String hosts,String sshUser,Integer sshPort,Integer page,Integer pageSize) {
        return installService.analysisHostList(clusterId,hosts,sshUser,sshPort,page,pageSize);
    }

    /**
     * 查询主机校验状态
     */
    @PostMapping("/getHostCheckStatus")
    @UserPermission
    public Result getHostCheckStatus(Integer clusterId,String sshUser,Integer sshPort) {
        return installService.getHostCheckStatus(clusterId,sshUser,sshPort);
    }

    /**
     * 重新进行主机环境校验
     */
    @PostMapping("/rehostCheck")
    @UserPermission
    public Result rehostCheck(Integer clusterId,String hostnames, String sshUser, Integer sshPort) {
        return installService.rehostCheck(clusterId,hostnames,sshUser,sshPort);
    }

    /**
     * 查询主机校验是否全部完成
     */
    @PostMapping("/hostCheckCompleted")
    @UserPermission
    public Result hostCheckCompleted(Integer clusterId) {
        return installService.hostCheckCompleted(clusterId);
    }
    /**
     * 主机管理agent分发安装进度列表
     */
    @PostMapping("/dispatcherHostAgentList")
    @UserPermission
    public Result dispatcherHostAgentList(Integer clusterId,Integer installStateCode,Integer page,Integer pageSize) {
        return installService.dispatcherHostAgentList(clusterId,installStateCode,page,pageSize);
    }

    /**
     * Agent节点分发
     * @param clusterId
     * @return
     */
    @PostMapping("/dispatcherHostAgentCompleted")
    public Result dispatcherHostAgentCompleted(Integer clusterId) {
        return installService.dispatcherHostAgentCompleted(clusterId);
    }

    /**
     * 主机管理agent分发取消
     */
    @PostMapping("/cancelDispatcherHostAgent")
    public Result cancelDispatcherHostAgent(Integer clusterId,String hostname,Integer installStateCode) {
        return installService.cancelDispatcherHostAgent(clusterId,hostname,installStateCode);
    }

    /**
     * 主机管理agent分发安装重试
     * @param clusterId
     * @param hostnames
     * @return
     */
    @PostMapping("/reStartDispatcherHostAgent")
    public Result reStartDispatcherHostAgent(Integer clusterId,String hostnames) {
        return installService.reStartDispatcherHostAgent(clusterId,hostnames);
    }
}
