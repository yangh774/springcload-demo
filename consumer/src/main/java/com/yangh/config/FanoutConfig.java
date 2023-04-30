package com.yangh.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Struct;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/01 下午 03:41
 */
@Configuration
public class FanoutConfig {

    //声明交换机 fanout = 扇出
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("itcast.fanout");
    }
    //声明队列1
    @Bean
    public Queue fanoutQueue() {
        return new Queue("fanout.queue1");
    }
    //绑定 把传进来的交换机和队列进行绑定 queue的方法名设置相同
    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }

    //声明队列2
    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }
    //绑定
    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    @Bean
    public DirectExchange simpleDirect() {
        // 交换机名字、是否持久化、未绑定队列是否删除
        return new DirectExchange("simple.direct", true, false);
    }
    @Bean
    public Queue simpleQueue() {
        // 交换机名字、是否持久化、未绑定队列是否删除
        return QueueBuilder.durable("simple.quque").build();
    }

    // 正常交换机
    @Bean
    public DirectExchange ttlDirect() {
        return new DirectExchange("ttl.direct");
    }

    // 队列绑定死信属性
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder.durable("ttl.quque")
                .ttl(10000) // 延迟时间
                .deadLetterExchange("dl.direct") // 指定死信交换机
                .deadLetterRoutingKey("dl")    // 指定死信交换机绑定队列的routingkey标识。
                .build();
    }
    // 队列正常交换机由他绑定
    @Bean
    public Binding ttlBind() {
        return BindingBuilder.bind(ttlQueue()).to(ttlDirect()).with("ttl");
    }




}
