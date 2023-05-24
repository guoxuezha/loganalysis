package com.gem.loganalysis.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author DuWeiHao
 * create 2021/12/15
 */
public class MapToBeanUtil {

    public static <T> List<T> execute(List<HashMap<String, String>> mapList, Class<T> beanClass) {
        List<T> result = new ArrayList<>();
        for (HashMap<String, String> map : mapList) {
            T obj = BeanUtil.mapToBean(map, beanClass, true, new CopyOptions().ignoreCase());
            result.add(obj);
        }
        return result;
    }

    public static <T> List<T> execute(Class<T> beanClass, List<HashMap<String, Object>> mapList) {
        List<T> result = new ArrayList<>();
        for (HashMap<String, Object> map : mapList) {
            T obj = BeanUtil.mapToBean(map, beanClass, true, new CopyOptions().ignoreCase());
            result.add(obj);
        }
        return result;
    }

    public static <T> List<T> executeCaseFalse(List<HashMap<String, String>> mapList, Class<T> beanClass) {
        List<T> result = new ArrayList<>();
        for (HashMap<String, String> map : mapList) {
            T obj = BeanUtil.mapToBean(map, beanClass, false, new CopyOptions().ignoreCase());
            result.add(obj);
        }
        return result;
    }
}

