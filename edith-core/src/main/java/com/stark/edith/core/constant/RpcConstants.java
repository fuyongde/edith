package com.stark.edith.core.constant;

/**
 * @author fuyongde
 * @date 2019/7/11
 */
public interface RpcConstants {

    /**
     * 最小的端口 0
     */
    int PORT_MIN = 0;
    /**
     * 最大的端口 65535
     */
    int PORT_MAX = ~(-1 << 16);

    /**
     * 默认核心线程数0
     */
    int THREAD_CORE_POOL_SIZE = 0;

    /**
     * 线程池大小为200
     */
    int THREAD_MAXIMUM_POOL_SIZE = 200;

    /**
     * 线程名称
     */
    String THREAD_NAME = "Edith-%d";
}
