package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: 日志索引信息
 * Date: 2023/4/25 19:03
 *
 * @author GuoChao
 **/
@TableName(value = "log_index")
@Data
@AllArgsConstructor
public class LogIndex {

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 联合主键格式
     */
    private String keyFormat;

    /**
     * 内容联合主键
     */
    private String unionKey;

    /**
     * 消息内容ID
     */
    private Integer logId;


}
