package com.yangh;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/11 下午 04:04
 */
@RestController
public class Fenbushisuo {
    // 注入
    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/fenbudemo")
    public String demo2() throws InterruptedException {
        // 创建一个简单的锁对象
        RLock simplelock = redissonClient.getLock("simplelock");
        /**
         *  获取锁
         *  waitTime 最大重试等待时间、leaseTime超时释放时间 TimeUnit
         *  参数是可选的、如果无参数、不等待（立即返回不重试）30s超时释放、
         */
        boolean lock = simplelock.tryLock(20,20, TimeUnit.SECONDS);
        if (lock) {
            try {
                Thread.sleep(3000);
                return "{200:锁获取成功}";
            } finally {
                // 释放锁
//                simplelock.unlock();
            }
        }else {
            return "{400:锁获取超时失败}";
        }
    }
    @RequestMapping("/fenbudemo2")
    public String demo3() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        pool.submit(()->{
            for (int i = 0; i < 100; i++) {
                try {
                    String s = demo2();
                    System.out.println(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return "s:s";
    }
}
