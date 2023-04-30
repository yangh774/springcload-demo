package com.yangh;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 该注解只能作用于方法上
@Retention(RetentionPolicy.RUNTIME) // 注解生命周期、运行时
@Documented // 用 @Documented 注解修饰的注解类会被 JavaDoc 工具提取成文档 生成帮助文档
public @interface InvestmentRequestUtilAnnotate {
    // 设置指定时间内不可以重复操作 以秒为单位
    long expire() default 2;
}