package com.gem.loganalysis.facility;


import com.gem.loganalysis.model.bo.RsyslogNormalMessage;

import java.util.HashMap;

/**
 * Description: 设备,用于定义不同设备之间相同的行为
 * 防火墙(firewall)、防毒墙(Poison proof wall)、探针(probe)、蜜罐(honeypot)
 * Date 2023/4/23 15:53
 *
 * @author GuoChao
 **/
public interface Facility {

    /**
     * 解析日志消息并转为JSON格式
     *
     * @param log 日志对象
     * @return 转换结果
     */
    HashMap<String, Object> parseMsgToJson(RsyslogNormalMessage log);

    /**
     * 将日志消息放入缓存
     *
     * @param log 日志消息体
     */
    void insertInCache(RsyslogNormalMessage log);

    /**
     * 释放缓存
     *
     * @return copy后的新对象
     */
    HashMap<String, Object> flushCache();

    /**
     * 查询缓存情况
     *
     * @return 描述信息
     */
    String getCacheInfo();

}
