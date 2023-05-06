package com.gem.loganalysis.model.bo;

import lombok.Data;

/**
 * Description: Rsyslog模板化输出格式
 * Date: 2023/4/21 9:07
 *
 * @author GuoChao
 **/
@Data
public class RsyslogNormalMessage {

    /**
     * 日志类型
     */
    private int logType;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 消息发生IP
     */
    private String host;

    /**
     * 日志优先级
     */
    private String severity;

    /**
     * 日志发生的子系统
     */
    private String facility;

    /**
     * 消息体
     */
    private String message;

    /**
     * 消息标签
     */
    private String tag;

}
