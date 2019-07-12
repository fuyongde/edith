package com.stark.edith.sample.provider.service;

import com.stark.edith.sample.api.HelloService;

/**
 * @author fuyongde
 * @date 2019/7/11
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        String result = "Hello " + name;
        System.out.println(Thread.currentThread().getName() + " " + result);
        return result;
    }
}
