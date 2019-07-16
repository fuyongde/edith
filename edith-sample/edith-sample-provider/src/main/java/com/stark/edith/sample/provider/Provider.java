package com.stark.edith.sample.provider;

import com.stark.edith.core.RpcFramework;

/**
 * @author fuyongde
 * @date 2019/7/11
 */
public class Provider {

    public static void main(String[] args) throws Exception {
        RpcFramework.export(new String[]{"com.stark.edith.sample.provider.service"}, 8888);
    }
}
