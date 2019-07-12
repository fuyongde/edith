package com.stark.edith.core;

import com.google.common.base.Strings;
import com.stark.edith.core.util.ThreadUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import static com.stark.edith.core.constant.RpcConstants.PORT_MAX;
import static com.stark.edith.core.constant.RpcConstants.PORT_MIN;

/**
 * @author fuyongde
 * @date 2019/7/6 18:08
 */
public class RpcFramework {

    private static final ExecutorService executorService = ThreadUtils.newCachedThreadPool(0);

    public static void export(final Object service, int port) throws Exception {
        Objects.requireNonNull(service, "Service instance is null!");

        if (port <= PORT_MIN || port > PORT_MAX) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
        System.out.println("Export service " + service.getClass().getName() + " on port " + port);
        ServerSocket server = new ServerSocket(port);
        for (; ; ) {
            System.out.println("application is running");
            Socket socket = server.accept();
            executorService.execute(() -> {
                try {
                    try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                        String methodName = input.readUTF();
                        Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                        Object[] arguments = (Object[]) input.readObject();
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
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
        if (Strings.isNullOrEmpty(host)) {
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
                    output.writeUTF(method.getName());
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