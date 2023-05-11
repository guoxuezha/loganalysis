package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.loganalysis.model.bo.MergeLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志内容
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志内容ID
     */
    @TableId(value = "LOG_ID", type = IdType.AUTO)
    private Integer logId;

    /**
     * 时间戳
     */
    @TableField("TIMESTAMP")
    private String timestamp;

    /**
     * 主机
     */
    @TableField("HOST")
    private String host;

    /**
     * 日志级别
     */
    @TableField("SEVERITY")
    private String severity;

    /**
     * 日志发送方
     */
    @TableField("FACILITY")
    private String facility;

    /**
     * 日志消息
     */
    @TableField("MESSAGE")
    private String message;

    /**
     * 标签
     */
    @TableField("TAG")
    private String tag;

    public Log(MergeLog message) {
        BeanUtil.copyProperties(message, this);
    }

}
