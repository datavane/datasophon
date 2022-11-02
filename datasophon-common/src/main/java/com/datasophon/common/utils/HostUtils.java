package com.datasophon.common.utils;


import cn.hutool.core.io.file.FileReader;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;

/**
 * 读取hosts文件
 *
 * @author gaodayu
 */
public abstract class HostUtils {
    private static final String HOSTS_PATH = "/etc/hosts";
    private static final String ENDL = String.format("%n");

    public static void read() {
        List<String> list = Arrays.stream(new FileReader(HOSTS_PATH).readString().split(ENDL))
                .filter(it -> !it.trim().matches("(^#.*)|(\\s*)"))
                .map(it -> it.replaceAll("#.*", "").trim().replaceAll("\\s+", "\t"))
                .collect(Collectors.toList());
        HashMap<String, String> ipHost = new HashMap<>();
        HashMap<String, String> hostIp = new HashMap<>();
        for (String str : list) {
            String[] split = str.split("\\s+");
            if (split.length == 3) {
                ipHost.put(split[0], split[2]);
                hostIp.put(split[2],split[0]);
            }
            if (split.length == 2) {
                ipHost.put(split[0], split[1]);
                hostIp.put(split[1],split[0]);
            }
        }
        CacheUtils.put(Constants.IP_HOST,ipHost);
        CacheUtils.put(Constants.HOST_IP,hostIp);
    }

    public static String findIp(String hostname) {
        List<String> list = Arrays.stream(new FileReader(HOSTS_PATH).readString().split(ENDL))
                .filter(it -> !it.trim().matches("(^#.*)|(\\s*)"))
                .map(it -> it.replaceAll("#.*", "").trim().replaceAll("\\s+", "\t"))
                .collect(Collectors.toList());
        String ip = "";
        for (String str : list) {
            String[] split = str.split("\\s+");
            if (split.length == 3) {
                if(split[2].equals(hostname)){
                    ip = split[0];
                }
            }
            if (split.length == 2) {
               if(split[1] == hostname){
                   ip = split[0];
               }
            }
        }
        return ip;
    }

    public static void main(String[] args) {
        HostUtils.read();

    }

}