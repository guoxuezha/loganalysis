package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 日志表对应的实体类
 * Date: 2023/4/25 19:04
 *
 * @author GuoChao
 **/
@TableName(value = "log")
@Data
@NoArgsConstructor
public class Log {

    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    private Integer logId;

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 消息发生IP
     */
    private String host;

    /**
     * 日志严重级别
     */
    private String severity;

    /**
     * 日志提供方
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

    public Log(RsyslogNormalMessage message) {
        BeanUtil.copyProperties(message, this);
    }

}
