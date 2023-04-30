package com.yangh;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/01 下午 02:37
 */
@Component
public class Demo {
    // 监听一个队列
    @RabbitListener(queues = "fanout.queue1")
    public void listenSimpleQueue(String msg) {
        System.out.println("监听到消息："+msg);
    }
    // 监听一个队列
    @RabbitListener(queues = "fanout.queue2")
    public void listenSimpleQueue2(String msg) {
        System.out.println("监听到消息："+msg);
    }

    // 声明 binding = 结合 声明出来的就是监听的 exchange默认就是Direct类型
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "itcast.direct"),
            key = {"red"}
    ))
    public void listenSimpleQueue3(String msg) {
        System.out.println("监听到消息："+msg);
    }

    // 监听中国下所有话题
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic.ququ1"),
            exchange = @Exchange(name = "itcast.topic2",type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenSimpleQueue4(String msg){
        System.out.println("监听来自中国的话题："+msg);
    }

    // 死信消费者
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "dl.queue"),
//            exchange = @Exchange(name = "dl.direct"),
//            key = "dl"
//    ))
//    public void listenSimpleQueue5(String msg){
//        System.out.println("延迟消息："+msg);
//    }

    // 延迟消息 不指定exchange即：direct类型
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue"),
            exchange = @Exchange(name = "delay.exchange", delayed = "true"),
            key = "delay"
    ))
    public void delayDemo(String msg,String userName) {
        System.out.println("监听到delayExchange的延迟消息："+msg+userName);
        List<Object> objects = Collections.synchronizedList(new ArrayList<>());
        
    }

    // 路由模式会抢夺消息吗？还是每人都有
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "guangboQueue"),
            exchange = @Exchange(name = "guangbo",type = ExchangeTypes.FANOUT)
    ))
    public void guangbo(String msg){
        System.out.println("广播："+msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "guangboQueue2"),
            exchange = @Exchange(name = "guangbo",type = ExchangeTypes.FANOUT)))
    public void guangbo2(String msg){
        System.out.println("广播2："+msg);
    }

    // 死信消费者
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "dl.queue2"),
            exchange = @Exchange(name = "dl.direct"),
            key = "dl"
    ))
    public void listenSimpleQueue55(String msg){
        System.out.println("死信消息："+msg);
    }

    // 是否无人消费就进入死心
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "ttl.quque"),
//            exchange = @Exchange(name = "guangbo",type = ExchangeTypes.FANOUT)))
//    public void listenSimpleQueue6(String msg){
//        System.out.println("正常消息："+msg);
//    }

}
