package com.yangh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/30 上午 11:56
 */
@Controller
public class Demo {
    @RequestMapping("/demo")
    public Object demo() {
        return "demo";
    }

}
