package com.gem.loganalysis.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 11:21
 */
public class CustomDateFormatUtil {

    public static String format(String date) {
        return DateUtil.format(DateUtil.parse(date), DatePattern.PURE_DATETIME_PATTERN);
    }

}
