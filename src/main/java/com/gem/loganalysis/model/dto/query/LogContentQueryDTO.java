package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/16 18:07
 */
@Data
public class LogContentQueryDTO {

    /**
     * 日主解析规则ID
     */
    private String ruleRelaId;

    /**
     * 日志更新日期
     */
    private String updateTime;

    /**
     * 归并日志ID
     */
    private String logId;

}
