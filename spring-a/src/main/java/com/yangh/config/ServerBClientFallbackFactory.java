//package com.yangh.config;
//
//import com.yangh.clinet.ServerBClient;
//import org.springframework.cloud.openfeign.FallbackFactory;
////import feign.hystrix.FallbackFactory;
///**
// * @author "杨航_2211"
// * @Descript {
// * 介绍：
// * }
// * @Project SpringCloud-Alibaba
// * @date 2023/03/31 下午 02:37
// */
//public class ServerBClientFallbackFactory implements FallbackFactory<ServerBClient> {
//    @Override
//    public ServerBClient create(Throwable cause) {
//        return () -> "降级处理";
//    }
//}
