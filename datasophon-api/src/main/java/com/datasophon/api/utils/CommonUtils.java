package com.datasophon.api.utils;

import cn.hutool.http.HttpUtil;
import com.datasophon.common.model.HostInfo;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.enums.InstallState;
import com.datasophon.dao.enums.RoleType;

import java.util.HashMap;

public class CommonUtils {
    public static void main(String[] args) {
//        Session session = JschUtil.createSession("172.31.96.16", 22, "root", "D:\\360Downloads\\id_rsa", null);
//        try {
//            session.connect(1000);
//            JschUtil.exec(session,"java -version", Charset.defaultCharset());
//        } catch (JSchException e) {
//            e.printStackTrace();
//        }
        String s = HttpUtil.get("http:/015:9093");
        System.out.println(s);
//        String s = HttpUtil.get("http://ddp251:27002/metrics");
//        System.out.println(s);
    }
    public static void updateProgress(Integer progress, HostInfo hostInfo) {
        HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(hostInfo.getClusterCode() + Constants.HOST_MAP);
        hostInfo.setProgress(progress);
        map.put(hostInfo.getHostname(),hostInfo);
    }

    public static void updateInstallState(InstallState installState, HostInfo hostInfo) {
        HashMap<String, HostInfo> map = (HashMap<String, HostInfo>) CacheUtils.get(hostInfo.getClusterCode() + Constants.HOST_MAP);
        hostInfo.setInstallState(installState);
        hostInfo.setInstallStateCode(installState.getValue());
        map.put(hostInfo.getHostname(),hostInfo);
    }

    public static void buildCommand(){

    }

    public static RoleType convertRoleType(String roleType) {
        switch (roleType){
            case "master":
                return RoleType.MASTER;
            case "worker":
                return RoleType.WORKER;
            case "client":
                return RoleType.CLIENT;
            default:
                return null;
        }
    }
}
