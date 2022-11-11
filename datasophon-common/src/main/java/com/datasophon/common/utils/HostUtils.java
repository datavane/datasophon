package com.datasophon.common.utils;


import cn.hutool.core.io.file.FileReader;
import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 读取hosts文件
 *
 * @author gaodayu
 */
public final class HostUtils {
    private static final String HOSTS_PATH = "/etc/hosts";

    private static final String ENDL = "\r?\n";

    private static final String ONE_OR_MORE_SPACE = "\\s+";
    private static final String COMMENT_OR_BLANK_LINE = "(^#.*)|(\\s*)";
    private static final String COMMENT = "#.*";
    private static final String TABS = "\t";

    public static void read() {
        List<String> ipHostNameList = parse2List();

        HashMap<String, String> ipHost = new HashMap<>();
        HashMap<String, String> hostIp = new HashMap<>();
        for (String str : ipHostNameList) {
            String[] split = str.split(ONE_OR_MORE_SPACE);
            int splitLength = split.length;
            if (splitLength < 2) {
                continue;
            }
            ipHost.put(split[0], split[splitLength - 1]);
            hostIp.put(split[splitLength - 1], split[0]);
        }

        CacheUtils.put(Constants.IP_HOST, ipHost);
        CacheUtils.put(Constants.HOST_IP, hostIp);
    }

    public static String findIp(String hostname) {
        List<String> ipHostNameList = parse2List();

        String ip = "";
        for (String str : ipHostNameList) {
            String[] split = str.split(ONE_OR_MORE_SPACE);
            if (split.length < 2) {
                continue;
            }
            if (Objects.equals(split[split.length - 1], hostname)) {
                ip = split[0];
                break;
            }
        }
        return ip;
    }

    private static List<String> parse2List() {
        List<String> list = Arrays.stream(new FileReader(HOSTS_PATH).readString().split(ENDL))
                .filter(it -> !it.trim().matches(COMMENT_OR_BLANK_LINE))
                .map(it -> it.replaceAll(COMMENT, "").trim().replaceAll(ONE_OR_MORE_SPACE, TABS))
                .collect(Collectors.toList());
        return list;
    }

}