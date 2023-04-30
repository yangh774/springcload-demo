package com.yangh;

import com.yangh.model.WenZhang;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2023/04/28 上午 09:08
 */
@Aspect
@Component
public class SpringAOPDemo {
    // 敏感字过滤器
    @Autowired
    SensitiveFilter sensitiveFilter;

    // 给使用这个注解的方法进行切入
    @Pointcut("@annotation(com.yangh.InvestmentRequestUtilAnnotate)")
    public void repeatSubmit(){}

    // 通知 Around 环绕 ProceedingJoinPoint连接点 仅支持环绕通知
    @Around("repeatSubmit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取到请求的参数列表
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            // 如果该参数是文章实体类
            if (arg instanceof WenZhang) {

                // 类型转换的简写语法、或者  WenZhang  wenzhang = （WenZhang） arg; 也可以
                String biaoti = ((WenZhang) (arg)).getBiaoti(); //获取文章类 的标题属性
                String neirong = ((WenZhang) (arg)).getNeirong(); //获取文章类 的内容属性

                // 不等于空就过滤一下
                if (biaoti != null) {
                    ((WenZhang) (arg)).setBiaoti(sensitiveFilter.filter(biaoti));
                }
                if (neirong != null) {
                    ((WenZhang) (arg)).setNeirong(sensitiveFilter.filter(neirong));
                }

            }
            // 如果能转为评论实体类...
        }
        // proceed = 继续
        return joinPoint.proceed();
    }
}
