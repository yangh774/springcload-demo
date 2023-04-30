package com.yangh.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/18 下午 08:48
 */
@ControllerAdvice // Advicev ad waisi 劝告的意思
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) //捕获异常类型、可以针对不同异常做出处理 而且可以自定义异常针对处理
    @ResponseBody // 返回结果转为json串
    public Object error(Exception e) {
        e.printStackTrace(); // 先打印一下
        return "{500:未知错误}";
    }



}
