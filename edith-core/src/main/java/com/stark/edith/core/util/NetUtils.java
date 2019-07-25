package com.stark.edith.core.util;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import static com.stark.edith.core.constant.RpcConstants.*;

/**
 * @author fuyongde
 * @date 2019/7/19
 */
public class NetUtils {

    private static final int RND_PORT_START = 30000;
    private static final int RND_PORT_RANGE = 10000;

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");
    private static volatile InetAddress LOCAL_ADDRESS = null;

    public static int getRandomPort() {
        return RND_PORT_START + ThreadLocalRandom.current().nextInt(RND_PORT_RANGE);
    }

    public static int getAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(null);
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            return getRandomPort();
        }
    }

    public static boolean isInvalidPort(int port) {
        return port <= PORT_MIN || port > PORT_MAX;
    }

    public static boolean isLocalHost(String host) {
        return host != null && (LOCAL_IP_PATTERN.matcher(host).matches() || host.equalsIgnoreCase(LOCALHOST_KEY));
    }

    /**
     * 获取本机的回旋地址
     *
     * @return 回旋地址
     */
    public static String getLoopbackAddress() {
        InetAddress inetAddress = InetAddress.getLoopbackAddress();
        return inetAddress.getHostAddress();
    }

    /**
     * 获取本机的局域网ip地址
     *
     * @return ip地址
     */
    public static String getLocalHostLANAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), getAvailablePort());
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
