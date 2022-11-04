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

    }


    public static void updateInstallState(InstallState installState, HostInfo hostInfo) {
        hostInfo.setInstallState(installState);
        hostInfo.setInstallStateCode(installState.getValue());
    }


    public static RoleType convertRoleType(String roleType) {
        switch (roleType) {
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
