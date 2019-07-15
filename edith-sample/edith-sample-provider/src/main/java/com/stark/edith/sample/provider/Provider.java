package com.stark.edith.sample.provider;

import com.stark.edith.core.RpcFramework;
import com.stark.edith.sample.api.DroneService;
import com.stark.edith.sample.api.HelloService;
import com.stark.edith.sample.provider.service.DroneServiceImpl;
import com.stark.edith.sample.provider.service.HelloServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * @author fuyongde
 * @date 2019/7/11
 */
public class Provider {

    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        DroneService droneService = new DroneServiceImpl();
        List<Object> lists = Arrays.asList(helloService, droneService);
        RpcFramework.export(lists, 8888);
    }
}
