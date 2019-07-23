package com.stark.edith.core.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

import static com.stark.edith.core.constant.RpcConstants.PORT_MAX;
import static com.stark.edith.core.constant.RpcConstants.PORT_MIN;

/**
 * @author fuyongde
 * @date 2019/7/19
 */
public class NetUtils {

    private static final int RND_PORT_START = 30000;
    private static final int RND_PORT_RANGE = 10000;

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
}
