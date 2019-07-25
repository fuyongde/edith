package com.stark.edith.core.util;

import org.junit.Test;

/**
 * @author fuyongde
 * @date 2019/7/19
 */
public class NetUtilsTest {

    @Test
    public void getRandomPort() {
        System.out.println(NetUtils.getRandomPort());
    }

    @Test
    public void getAvailablePort() {
        System.out.println(NetUtils.getAvailablePort());
    }

    @Test
    public void isInvalidPort() {
        System.out.println(NetUtils.isInvalidPort(-1));
    }

    @Test
    public void getLoopbackAddress() {
        System.out.println(NetUtils.getLoopbackAddress());
    }

    @Test
    public void getLocalHostLANAddress() {
        System.out.println(NetUtils.getLocalHostLANAddress());
    }

}