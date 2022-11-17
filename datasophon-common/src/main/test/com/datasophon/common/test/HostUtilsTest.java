package com.datasophon.common.test;

import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.common.utils.HostUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author rwj
 * @since 2022/11/11
 */
public class HostUtilsTest {

    @Test
    public void testReadHotsFile() {
        HostUtils.read();
        Map<String, String> ipHostMap = (Map<String, String>) CacheUtils.get(Constants.IP_HOST);
        ipHostMap.forEach((k, v) -> System.out.println(k + " --- " + v));
    }

    @Test
    public void testFindIp() {
        String ip = HostUtils.findIp("ddh1.test.cn");
        Assert.assertEquals("192.168.2.101", ip);
    }

}
