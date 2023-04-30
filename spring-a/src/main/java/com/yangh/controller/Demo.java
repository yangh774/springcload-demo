package com.yangh.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/16 下午 02:54
 */
public class Demo {
    public static void main(String[] args) throws ParseException {
            String dangq = "2022-10-10 08:10:10";
            String wanghou = "2022-10-10 08:10:20";
        Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dangq);
        Date parse2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(wanghou);
        long time = parse.getTime();
        long time1 = parse2.getTime();
        long ime3 = time1-time;
        System.out.println(ime3);
    }
}
