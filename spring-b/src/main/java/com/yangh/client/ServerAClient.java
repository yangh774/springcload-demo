package com.yangh.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/12 上午 09:57
 */
@FeignClient("alibaba-a")
public interface ServerAClient {
    @GetMapping("/jianqian")
    String suiyi(@RequestParam("addUser") String addUser,@RequestParam("money") String money);
}