package com.yangh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/03/31 上午 11:49
 */
@SpringBootApplication
@MapperScan({"com.yangh.mapper"})
//@ComponentScan(basePackages = {"com.yangh.controler"})
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
//        服务A ID192.168.137.106:8091:1999980224244621364
//        服务B ID192.168.137.106:8091:1999980224244621364
