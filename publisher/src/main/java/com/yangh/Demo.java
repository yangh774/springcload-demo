package com.yangh;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/15 下午 03:09
 */
@Controller
public class Demo {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/demo")
    public void demo() {
        rabbitTemplate.convertAndSend("ttl.direct","ttl","消息测试");
    }
    @RequestMapping("/demo2")
    public void demo2() {
        rabbitTemplate.convertAndSend("guangbo","","message");
    }

    @RequestMapping("/demo3")
    public void demo3() {
        Message build = MessageBuilder
                .withBody("message body".getBytes(StandardCharsets.UTF_8))
                .setExpiration("2000") // 给这条消息设置超时时间
                .build();
        rabbitTemplate.convertAndSend("ttl.direct","ttl",build);
    }


}
