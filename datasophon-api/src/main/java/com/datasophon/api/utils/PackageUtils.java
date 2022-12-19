package com.datasophon.api.utils;

import com.datasophon.common.Constants;

import java.util.HashMap;

/**
 *
 *
 * @author zhangqiao
 * @email 13707421712@163.com
 * @date 2022-12-17 12:33
 * @Description: PackageUtils工具类
 */
public class PackageUtils {

    static HashMap<String, String> map = new HashMap<String,String>();

    public static void putServicePackageName(String frameCode, String serviceName, String dcPackageName) {
        map.put(frameCode + Constants.UNDERLINE + serviceName, dcPackageName);
    }


    public static String getServiceDcPackageName(String frameCode, String serviceName) {
        return map.get(frameCode + Constants.UNDERLINE + serviceName);
    }
}
