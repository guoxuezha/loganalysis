package com.gem.loganalysis.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/20 1:39
 */
public class CustomDateUtil {

    public static List<String> getBetweenDateList(DateTime startDate, DateTime endDate) {
        List<String> dateList = new ArrayList<>();
        long l = DateUtil.betweenDay(startDate, endDate, false);
        for (int i = 1; i <= l; i++) {
            dateList.add(DateUtil.offset(startDate, DateField.DAY_OF_YEAR, i).toString("yyyy-MM-dd"));
        }
        return dateList;
    }

    public static List<DateTime> getBetweenTimeList(DateTime endTime, int size) {
        List<DateTime> result = new ArrayList<>();
        for (int i = size; i > 0; i--) {
            result.add(DateUtil.offset(endTime, DateField.MINUTE, -i));
        }
        return result;
    }

}
