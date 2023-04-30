package com.yangh.clinet;

//import com.yangh.config.ServerBClientFallbackFactory;
//import com.yangh.config.ServerBClientFallbackFactoty2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/03/31 上午 11:59
 */
@FeignClient(value = "alibaba-b")
public interface ServerBClient {
    @GetMapping("/serverb")
    String suibian();
}
