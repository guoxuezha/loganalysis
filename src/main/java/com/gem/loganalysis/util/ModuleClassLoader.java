package com.gem.loganalysis.util;

import com.gem.loganalysis.model.HandleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类描述 动态加载外部jar包的自定义类加载器,用于管理各单独Jar包解析后的内容
 *
 * @author GuoChao
 * @version 1.0
 * fileName ModuleClassLoader.java
 * @date 2019-06-21 10:22
 */
@Slf4j
public class ModuleClassLoader extends URLClassLoader {

    /**
     * 本类加载器的名称(全局唯一)
     */
    private final String moduleName;

    /**
     * 版本号
     */
    private final String version;

    /**
     * 保存本类加载器加载的class字节码
     */
    private final Map<String, byte[]> classBytesMap = new HashMap<>();

    /**
     * 已经加载到JVM中的Class对象
     */
    private final Map<String, Class<?>> cacheClassMap = new HashMap<>();

    /**
     * 已经注册的spring bean的name集合
     */
    private final List<String> registeredBean = new ArrayList<>();

    /**
     * 属于本类加载器加载的jar包
     */
    private JarFile jarFile;

    /**
     * 构造方法
     *
     * @param urls       JarFile句柄
     * @param parent     父级类加载器
     * @param moduleName 模块名
     * @param version    版本
     */
    public ModuleClassLoader(URL[] urls, ClassLoader parent, String moduleName, String version) {
        super(urls, parent);
        this.moduleName = moduleName;
        this.version = version;
        URL url = urls[0];
        String path = url.getPath();
        try {
            jarFile = new JarFile(path);
            log.info("Jar包中文件条目数为: {}", jarFile.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 初始化类加载器执行类加载
        init();
    }

    public int getCacheClassSize() {
        return cacheClassMap.size();
    }

    /**
     * 改写loadClass方式,
     * 若该类是第一次在Jar包中被遍历到,则将其加载到JVM中
     * 若该类是第N次(N>1)在Jar包中被遍历到,则跳过加载,并返回第一次出现的类
     * 若该类是第一次在Jar包中被遍历到,但该类在JVM中已存在,则返回NULL
     *
     * @param name 类名
     * @return 类对象
     * @throws ClassNotFoundException 找不到目标类
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (findLoadedClass(name) == null) {
            return super.loadClass(name);
        } else {
            return cacheClassMap.get(name);
        }
    }

    /**
     * 方法描述 初始化类加载器，保存字节码
     */
    private void init() {
        // 解析Class字节码并将其放入Map
        analysisClass();
        log.info("{} Class 字节码文件个数为 : {}", moduleName, classBytesMap.size());

        // 将jar中的每一个class字节码进行Class载入JVM
        for (Map.Entry<String, byte[]> entry : classBytesMap.entrySet()) {
            String key = entry.getKey();
            Class<?> aClass = null;
            try {
                aClass = loadClass(key);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            cacheClassMap.put(key, aClass);
        }
        log.info("加载到JVM中的字节码文件个数为: {}", cacheClassMap.size());
    }

    /**
     * 解析Jar包中的Class文件,将其放入Map
     */
    private void analysisClass() {
        Enumeration<JarEntry> en = jarFile.entries();
        InputStream input = null;
        try {
            while (en.hasMoreElements()) {
                JarEntry je = en.nextElement();
                String name = je.getName();
                // 这里添加了路径扫描限制
                if (name.endsWith(".class")) {
                    String className = name.replace(".class", "").replaceAll("/", ".");
                    input = jarFile.getInputStream(je);
                    ByteArrayOutputStream base = new ByteArrayOutputStream();
                    int bufferSize = 4096;
                    byte[] buffer = new byte[bufferSize];
                    int bytesNumRead;
                    while ((bytesNumRead = input.read(buffer)) != -1) {
                        base.write(buffer, 0, bytesNumRead);
                    }
                    byte[] classBytes = base.toByteArray();
                    classBytesMap.put(className, classBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 方法描述 初始化spring bean
     */
    public HandleDTO<Object> initBean() {
        if (cacheClassMap == null || cacheClassMap.size() == 0) {
            return HandleDTO.fail("从Jar包中解析出的Class对象列表为空!");
        }
        int springBeanNum = 0;
        for (Map.Entry<String, Class<?>> entry : cacheClassMap.entrySet()) {
            String className = entry.getKey();
            // Bean名: 类加载器名称 + 类名
            String beanName = moduleName + "-" + version + className.substring(className.lastIndexOf(".") + 1);
            Class<?> cla = entry.getValue();
            HandleDTO<String> beanClass = isSpringBeanClass(cla);
            if (beanClass.getSuccess()) {
                log.info("注册SpringBean: {} 判定其为SpringBean的依据为: {}", beanName, beanClass.getMessage());
                springBeanNum++;
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cla);
                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
                // 设置当前bean定义对象是单例的
                beanDefinition.setScope("singleton");
                SpringContextUtil.getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
                registeredBean.add(beanName);
            }
        }
        return HandleDTO.success("从Jar包中解析出的SpringBean对象数量为: " + springBeanNum, registeredBean);
    }

    /**
     * 获取当前类加载器注册的bean
     * 在移除当前类加载器的时候需要手动删除这些注册的bean
     *
     * @return 该加载器已加载进JVM中的SpringBean
     */
    public List<String> getRegisteredBean() {
        return registeredBean;
    }

    /**
     * 方法描述 判断class对象是否带有spring的注解
     *
     * @param cla jar中的每一个class
     * @return true 是spring bean   false 不是spring bean
     */
    public HandleDTO<String> isSpringBeanClass(Class<?> cla) {
        if (cla == null) {
            return HandleDTO.fail("class为Null");
        }
        //是否是接口
        if (cla.isInterface()) {
            return HandleDTO.fail("class为接口");
        }

        //是否是抽象类
        if (Modifier.isAbstract(cla.getModifiers())) {
            return HandleDTO.fail("class为抽象类");
        }

        if (cla.getAnnotation(Component.class) != null) {
            return HandleDTO.success("Component");
        }
        if (cla.getAnnotation(Repository.class) != null) {
            return HandleDTO.success("Repository");
        }
        if (cla.getAnnotation(Service.class) != null) {
            return HandleDTO.success("Service");
        }
        if (cla.getAnnotation(RestController.class) != null) {
            return HandleDTO.success("RestController");
        }
        return HandleDTO.fail("检查完成,非SpringBean!");
    }

    /**
     * 根据报名与接口Class获取实现类信息
     *
     * @param packageName    包名
     * @param superClassName 接口全类名
     * @return 实现类信息
     */
    public Set<Class<?>> scanPackageBySuper(String packageName, String superClassName) {
        Set<Class<?>> result = new HashSet<>();
        for (Class<?> clazz : cacheClassMap.values()) {
            if (clazz == null) {
                continue;
            }
            if (clazz.getName().startsWith(packageName + ".")) {
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> aClass : interfaces) {
                    if (superClassName.equals(aClass.getName())) {
                        result.add(clazz);
                    }
                }
            }
        }
        return result;
    }

}
