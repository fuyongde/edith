package com.stark.edith.core.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

import static com.stark.edith.core.constant.RpcConstants.*;

/**
 * @author fuyongde
 * @date 2019/7/12
 */
public class ThreadUtils {

    private static ThreadFactory buildThreadFactory(String format) {
        return new ThreadFactoryBuilder().setNameFormat(format).build();
    }

    public static ExecutorService newCachedThreadPool(int maximumPoolSize) {
        return new ThreadPoolExecutor(THREAD_CORE_POOL_SIZE, maximumPoolSize > 0 ? maximumPoolSize : THREAD_MAXIMUM_POOL_SIZE,
                Long.MAX_VALUE, TimeUnit.MICROSECONDS, new SynchronousQueue<>(), buildThreadFactory(THREAD_NAME), new ThreadPoolExecutor.AbortPolicy());
    }
}
