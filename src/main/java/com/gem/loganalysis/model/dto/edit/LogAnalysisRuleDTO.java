package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 21:44
 */
@Data
public class LogAnalysisRuleDTO {

    /**
     * 规则ID
     */

    @ApiModelProperty("规则ID")
    private String analysisRuleId;

    /**
     * 规则类型(1配置类规则 2硬编码规则)
     */
    @ApiModelProperty("规则类型(1配置类规则 2硬编码规则)")
    private Integer ruleType;

    /**
     * 硬编码方法名
     */
    @ApiModelProperty("硬编码方法名")
    private String methodName;

    /**
     * 分段字符
     */
    @ApiModelProperty("分段字符")
    private String itemSplit;

    /**
     * 分值字符
     */
    @ApiModelProperty("分值字符")
    private String kvSplit;

}
