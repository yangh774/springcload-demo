package com.yangh;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/03/31 上午 11:42
 */
@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
@MapperScan({"com.yangh.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
//    @Bean
//    public ServerBClientFallbackFactory serverBClientFallbackFactory() {
//        return new ServerBClientFallbackFactory();
//    }
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.47.128:6379").setPassword("yh123");
        return Redisson.create(config);
    }

}
