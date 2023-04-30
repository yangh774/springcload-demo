package com.yangh.controller;

import com.yangh.mapper.UserBalanceaddition;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/12 上午 09:45
 */
@RestController
public class UserCaoZuo {
    @Autowired(required = false)
    UserBalanceaddition userBalanceadditionl;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate restTemplate;

    //    @Autowired
//
    @GetMapping("/jianqian")
    @Transactional
    public String jiaqian(@RequestParam("addUser") String addUser, @RequestParam("money") String money) {
        int i = userBalanceadditionl.userBalanceAddition(addUser, money);
//        restTemplate.postForEntity()
        System.out.println("服务A ID" + RootContext.getXID());
        return i + "";
    }

    // 日活量实现
    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        // 页面加载的时候、关于首页的相关信息
        // 然后存到redis
        String date = new SimpleDateFormat("yyyy-MM-hh").format(new Date());
        System.out.println(request.getRemoteHost());
        System.out.println(request.getRemoteUser());
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getRemotePort());
        String header = request.getHeader("x-forwarded-for");
        System.out.println(header);
        redisTemplate.opsForHyperLogLog().add("hyperloglog:" + date, request.getRemoteHost());
        Long size = redisTemplate.opsForHyperLogLog().size("hyperloglog:" + date);
        return "{访客量:" + size + "}";
    }

    // 现在我就是投资、怎么保证幂等性、告诉我？ 幂等性核心是什么？就是你点十次 那也只执行一次

    @GetMapping("/touzi")
    public String touzi(Integer integer) {
        Boolean aNull = redisTemplate.opsForValue().setIfAbsent("mideng", integer, 3, TimeUnit.SECONDS);
        if (!aNull) {
            // 投资
            return "{00:请勿频繁提交}";
        }
        userBalanceadditionl.touziSql();
        return "{投资:成功啦}";
    }

    @GetMapping("/touzi2")
    @InvestmentRequestUtilAnnotate(expire = 3)
    public String touzi2() {
        userBalanceadditionl.touziSql();
        return "{投资:成功啦}";
    }
}
