package com.yangh.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/16 上午 10:58
 */
// 处理重复提交切面
@Component
@Aspect
public class RepeatSubmitAspect {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;

    // 定义切点 给使用这个注解的方法进行注入、而不是具体哪一个方法、非常的灵活
    @Pointcut("@annotation(com.yangh.controller.InvestmentRequestUtilAnnotate)")
    public void repeatSubmit(){}

    // 通知 Around 环绕 ProceedingJoinPoint连接点 仅支持环绕通知
    @Around("repeatSubmit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 从上下文获取属性
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取请求
        HttpServletRequest request = requestAttributes.getRequest();
        // 通过进程连接点 获取签名方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取注解上设置的时间
        long annotationExpireTime = method.getAnnotation(InvestmentRequestUtilAnnotate.class).expire();

        // 获取前端携带的token 通过拼接生成一个独有的key
        String investToken = request.getHeader("UserLoginStatusToken");
        investToken = "666";
        // 虽然网关过了一遍、但是该判断还是判断一下
        if (investToken == null) {
            // 直接报错是不是太粗鲁了呢？能否返回一个 ResultMessage 毕竟投资也是返回的ResultMessage 真的可以！
            return "{200:未登录}";
//            throw new RuntimeException("未检测到token、请登录");
        }

        // 开始拼装一个key 这个请求路径又是一个复用提升、如果单单是token、那可能会被其他业务影响、其他业务也有可能用token当key
        String requestURI = request.getRequestURI();
        String redisKey = "repeat_submit_key:".concat(requestURI).concat(investToken);

        // 判断redis里面如果不存在 key！ 这里存在并发问题、一下子
        RLock shubmitLock = redissonClient.getLock("shubmitLock");
        boolean b = shubmitLock.tryLock(5, 10, TimeUnit.SECONDS);
//        boolean b = shubmitLock.tryLock();
        if (b) {
            if (!redisTemplate.hasKey(redisKey)) {
                try {
                redisTemplate.opsForValue().set(redisKey,redisKey,annotationExpireTime, TimeUnit.SECONDS);
                    // proceed = 继续
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    redisTemplate.delete(redisKey); // 如果捕捉到异常是要删除这个key的
                    throw new Throwable(e); // 抛异常
                }finally {
                    shubmitLock.unlock();
                }
            }else {
                throw new Throwable("请勿重复提交");
            }
        }else {
            return "{00:拿不到锁}";
        }
    }
}
