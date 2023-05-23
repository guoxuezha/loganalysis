package com.gem.loganalysis.model.dto.edit;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/15 23:00
 */
@Data
public class LogAnalysisRuleRelaDTO {

    private String ruleRelaId;

    /**
     * 资产唯一编码
     */
    private String assetId;

    /**
     * 日志级别
     */
    private String severity;

    /**
     * 产生日志的子系统
     */
    private String facility;

    /**
     * 日志分析规则编码
     */
    private String analysisRuleId;

    /**
     * 归并字段属性列表
     */
    private String mergeItems;

    /**
     * 归并窗口时长（分钟）
     */
    private Integer mergeWindowTime;

    /**
     * 事件判定窗口时长（分钟）
     */
    private Integer eventWindowTime;

    /**
     * 事件判定阈值
     */
    private Integer eventThreshold;

    /**
     * 事件判定关键字或正则表达式
     */
    private String eventKeyword;

    private String eventTypeItem;

    private String eventClassItem;

}
