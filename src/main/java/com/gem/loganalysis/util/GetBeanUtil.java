package com.gem.loganalysis.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 在线程的run方法中使用注解@autoware注入的bean，会报空指针异常，原因是因为线程中为了线程安全，防注入。
 * 获取bean实例的工具类组件
 */
@Component
public class GetBeanUtil implements ApplicationContextAware {
    //Spring应用上下文环境
    private static ApplicationContext applicationContext;
    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    public void setApplicationContext(ApplicationContext context) {
        GetBeanUtil.applicationContext = context;
    }
    /**
     * 获取对象 这里重写了bean方法，起主要作用
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

