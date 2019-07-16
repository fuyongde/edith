package com.stark.edith.sample.provider.service;

import com.stark.edith.core.annotation.Service;
import com.stark.edith.sample.api.DroneService;

import java.util.Random;

/**
 * @author fuyongde
 * @date 2019/7/15
 */
@Service
public class DroneServiceImpl implements DroneService {

    @Override
    public boolean attack(String target) {
        Random random = new Random();
        boolean result = random.nextBoolean();
        System.out.println(Thread.currentThread().getName() + " Attack target " + result);
        return result;
    }
}
