package com.yangh.controller;

import com.yangh.client.ServerAClient;
import com.yangh.mapper.UserBalancedecrease;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/03/31 下午 12:00
 */
@RestController
public class Demo {
    @Autowired(required = false)
    UserBalancedecrease userBalancedecrease;
    @Autowired
    ServerAClient serverAClient;

    @GetMapping("/serverb")
    public String demo1() throws InterruptedException {
        Thread.sleep(3000);
        return "我是服务B";
    }

    // 收到转账请求
    @RequestMapping("/zhuanzhang")
    @GlobalTransactional(rollbackFor = Exception.class) // 入口添加全局事务
    @Transactional
    public String demo3(String addUser,String reduceUser,String money) throws TransactionException {
        // 减少
        int i = userBalancedecrease.userBalanceDecrease(reduceUser, money);
        // 添加
        serverAClient.suiyi(addUser, money);
        System.out.println("服务B ID"+ RootContext.getXID());
        if (i == 0) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            throw  new RuntimeException("余额不足无法转账");
        }
        return "尝试";
    }
}
