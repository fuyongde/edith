package com.stark.edith.sample.consumer;

import com.stark.edith.core.RpcFramework;
import com.stark.edith.sample.api.HelloService;

/**
 * @author fuyongde
 * @date 2019/7/11
 */
public class Consumer {
    public static void main(String[] args) {
        HelloService helloService = RpcFramework.refer(HelloService.class, "127.0.0.1", 8888);
        String name = "Peter Parker";
        System.out.println(helloService.hello(name));
    }
}
