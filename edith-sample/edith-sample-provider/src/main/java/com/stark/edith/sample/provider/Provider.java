package com.stark.edith.sample.provider;

import com.stark.edith.core.RpcFramework;
import com.stark.edith.sample.api.HelloService;
import com.stark.edith.sample.provider.service.HelloServiceImpl;

/**
 * @author fuyongde
 * @date 2019/7/11
 */
public class Provider {

    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        RpcFramework.export(helloService, 8888);
    }
}
