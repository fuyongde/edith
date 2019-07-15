package com.stark.edith.core;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.stark.edith.core.util.CollectionUtils;
import com.stark.edith.core.util.StringUtils;
import com.stark.edith.core.util.ThreadUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static com.stark.edith.core.constant.RpcConstants.PORT_MAX;
import static com.stark.edith.core.constant.RpcConstants.PORT_MIN;

/**
 * @author fuyongde
 * @date 2019/7/6 18:08
 */
public class RpcFramework {

    private static final ExecutorService executorService = ThreadUtils.newCachedThreadPool(0);

    private static final Map<String, Object> SERVICE_MAP = Maps.newHashMap();
    private static final String INTERFACE_NAME = "interfaceName";
    private static final String METHOD_NAME = "methodName";

    public static void export(final List<Object> services, int port) throws Exception {
        if (CollectionUtils.isEmpty(services)) {
            throw new IllegalArgumentException("Collection services is empty");
        }
        if (port <= PORT_MIN || port > PORT_MAX) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
        services.forEach(service -> System.out.println("Export service " + service.getClass().getName() + " on port " + port));
        services.forEach(service -> Stream.of(service.getClass().getInterfaces())
                .forEach(o -> SERVICE_MAP.put(o.getName(), service)));

        ServerSocket server = new ServerSocket(port);
        for (; ; ) {
            Socket socket = server.accept();
            executorService.execute(() -> {
                try {
                    try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                        String interfaceAndMethod = input.readUTF();
                        String interfaceName = JSON.parseObject(interfaceAndMethod).getString(INTERFACE_NAME);
                        String methodName = JSON.parseObject(interfaceAndMethod).getString(METHOD_NAME);
                        Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                        Object[] arguments = (Object[]) input.readObject();
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        try {
                            Object service = SERVICE_MAP.get(interfaceName);
                            Method method = service.getClass().getMethod(methodName, parameterTypes);
                            Object result = method.invoke(service, arguments);
                            output.writeObject(result);
                        } catch (Throwable t) {
                            output.writeObject(t);
                        } finally {
                            output.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) {
        Objects.requireNonNull(interfaceClass, "Interface class is null!");
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
        }
        if (StringUtils.isNullOrEmpty(host)) {
            throw new IllegalArgumentException("Host is null!");
        }
        if (port <= PORT_MIN || port > PORT_MAX) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
        System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, (proxy, method, arguments) -> {
            try (Socket socket = new Socket(host, port)) {
                try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                    Map<String, String> interfaceAndMethod = Maps.newHashMap();
                    interfaceAndMethod.put(INTERFACE_NAME, interfaceClass.getName());
                    interfaceAndMethod.put(METHOD_NAME, method.getName());
                    output.writeUTF(JSON.toJSONString(interfaceAndMethod));
                    output.writeObject(method.getParameterTypes());
                    output.writeObject(arguments);
                    try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                        Object result = input.readObject();
                        if (result instanceof Throwable) {
                            throw (Throwable) result;
                        }
                        return result;
                    }
                }
            }
        });
    }
}