package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 21:38
 */
@Data
public class AnalysisRuleQueryDTO {

    /**
     * 资产ID
     */
    private Integer assetId;

    /**
     * 规则类型
     */
    private Integer ruleType;

    /**
     * 安全等级
     */
    private String severity;

    /**
     * 子系统
     */
    private String facility;

}
