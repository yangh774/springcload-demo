package com.yangh.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/02 下午 01:13
 */
@Slf4j
@Configuration
public class RabbitMQReturnCallback implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取**ReturnCallback**
        RabbitTemplate bean = applicationContext.getBean(RabbitTemplate.class);
        bean.setReturnsCallback(returned -> {
            log.error("消息发送到队列失败");
        });
    }
}
