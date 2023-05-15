package com.gem.loganalysis.util;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

import static cn.hutool.extra.spring.SpringUtil.getApplicationContext;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/11 18:28
 */
@Component
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static DefaultListableBeanFactory getBeanFactory() {
        return (DefaultListableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据 服务名称, 方法名 反射调用  spring bean 中的 方法
     *
     * @param className  服务名
     * @param methodName 方法名
     * @param parameters 参数
     * @return 反射方法的返回值
     */
    public static Object invokeClassMethod(String className, String methodName, Object... parameters) {
        Object[] params = new Object[parameters.length];
        System.arraycopy(parameters, 0, params, 0, params.length);
        Class<?>[] paramClass = ClassUtil.getClasses(params);
        Object service = getBean(className);
        // 找到方法
        Method method = ReflectionUtils.findMethod(service.getClass(), methodName, paramClass);
        // 执行方法
        if (method != null) {
            return ReflectionUtils.invokeMethod(method, service, params);
        } else {
            log.warn("未能找到目标方法: {}.{} ", className, methodName);
            return null;
        }
    }

    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

}
