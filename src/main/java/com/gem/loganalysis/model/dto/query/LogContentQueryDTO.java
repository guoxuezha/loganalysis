package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/16 18:07
 */
@Data
@AllArgsConstructor
public class LogContentQueryDTO {

    /**
     * 日志解析规则ID
     */
    @ApiModelProperty("日志解析规则ID")
    private String ruleRelaId;

    /**
     * 日志更新日期
     */
    @ApiModelProperty("日志更新日期")
    private String updateTime;

    /**
     * 归并日志ID
     */
    @ApiModelProperty("归并日志ID")
    private String logId;

}
