package com.gem.loganalysis.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GuoChao
 * 自定义类加载器仓库
 */
@Slf4j
public class ClassloaderRepository {

    private final Map<String, ModuleClassLoader> repositoryMap = new ConcurrentHashMap<>();

    private ClassloaderRepository() {
    }

    public static ClassloaderRepository getInstance() {
        return ClassloaderRepositoryHolder.INSTANCE;
    }

    public Map<String, ModuleClassLoader> getRepositoryMap() {
        return repositoryMap;
    }

    public Set<Class<?>> scanPackageBySuper(String packageName, String superClass) {
        Set<Class<?>> result = new HashSet<>();
        for (ModuleClassLoader classLoader : repositoryMap.values()) {
            result.addAll(classLoader.scanPackageBySuper(packageName, superClass));
        }
        return result;
    }

    public void addClassLoader(String moduleName, ModuleClassLoader moduleClassLoader) {
        repositoryMap.put(moduleName, moduleClassLoader);
    }

    public boolean containsClassLoader(String key) {
        return repositoryMap.containsKey(key);
    }

    public String searchOldVersionClassLoaderByModuleName(String moduleName) {
        for (String s : repositoryMap.keySet()) {
            if (s.startsWith(moduleName)) {
                return s;
            }
        }
        return null;
    }

    public ModuleClassLoader getClassLoader(String key) {
        return repositoryMap.get(key);
    }

    /**
     * 根据名称删除类加载器
     *
     * @param moduleName 模块名(JarFileName)
     */
    public void removeClassLoader(String moduleName) {
        ModuleClassLoader moduleClassLoader = repositoryMap.get(moduleName);
        try {
            List<String> registeredBean = moduleClassLoader.getRegisteredBean();
            for (String beanName : registeredBean) {
                log.info("删除bean:" + beanName);
                SpringContextUtil.getBeanFactory().removeBeanDefinition(beanName);
            }
            moduleClassLoader.close();
            repositoryMap.remove(moduleName);
        } catch (IOException e) {
            log.error("删除" + moduleName + "模块发生错误");
        }
    }

    private static class ClassloaderRepositoryHolder {
        private static final ClassloaderRepository INSTANCE = new ClassloaderRepository();
    }

}
