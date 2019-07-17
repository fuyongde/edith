package com.stark.edith.core;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.stark.edith.core.annotation.Service;
import com.stark.edith.core.util.ArrayUtils;
import com.stark.edith.core.util.StringUtils;
import com.stark.edith.core.util.ThreadUtils;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
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

    public static void export(String[] packageNames, int port) throws Exception {
        if (ArrayUtils.isEmpty(packageNames)) {
            throw new IllegalArgumentException("PackageNames is empty");
        }
        if (port <= PORT_MIN || port > PORT_MAX) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
        Set<Object> services = Optional
                .ofNullable(initAllServices(packageNames))
                .orElseThrow(() -> new RuntimeException("There is no service with com.stark.edith.core.annotation.Service"));
        services.forEach(service -> System.out.println("Export service " + service.getClass().getName() + " on port " + port));
        services.stream()
                //若存在注解且要将服务进行暴露，则返回true
                .filter(service -> {
                    Service annotation = service.getClass().getAnnotation(Service.class);
                    return Objects.nonNull(annotation) && annotation.export();
                })
                //将服务按照 key=interfaceName，value=implObject的方式进行存储
                .forEach(service -> Stream.of(service.getClass().getInterfaces()).forEach(o -> SERVICE_MAP.put(o.getName(), service)));

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
                        Object service = SERVICE_MAP.get(interfaceName);
                        if (Objects.isNull(service)) {
                            output.writeObject(new RuntimeException("No such service : " + interfaceName));
                        }
                        try {
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

    private static Set<Object> initAllServices(String[] packageNames) {
        if (Objects.isNull(packageNames) || packageNames.length == 0) {
            return null;
        }

        return Stream.of(packageNames)
                .map(packageName -> new Reflections(packageName))
                .flatMap(reflections -> reflections.getTypesAnnotatedWith(Service.class).stream())
                .map(clazz -> {
                    try {
                        return clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }
}