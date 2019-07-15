package com.stark.edith.sample.api;

/**
 * @author fuyongde
 * @date 2019/7/15
 */
public interface DroneService {

    /**
     * 攻击目标
     *
     * @param target 目标
     * @return true=成功|false=失败
     */
    boolean attack(String target);
}
