package com.yangh.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/02 下午 02:46
 */
@Configuration
public class ErrorMessageConfig {

    // 定义失败的queue exchange 绑定在一起 设置MessageRecoverer接口
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        // 三个实现类对应不同的策略 这里选择投递到失败的交换机。或者重新入队也可以 路由模式所以加key
        return new RepublishMessageRecoverer(rabbitTemplate, "error.exchange", "key");

    }

}