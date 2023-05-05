package com.gem.loganalysis.model.vo;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/5 9:02
 */
@Data
public class EquipAnalysisRuleVO {

    private Integer equipId;

    private String equipName;

    private String severity;

    private String facility;

    private Integer ruleType;

    private String methodName;

    private String itemSplit;

    private String kvSplit;

}
