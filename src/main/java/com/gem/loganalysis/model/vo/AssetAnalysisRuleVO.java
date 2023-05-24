package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/5 9:02
 */
@Data
public class AssetAnalysisRuleVO {

    /**
     * 资产ID
     */
    @ApiModelProperty("资产ID")
    private String assetId;

    @ApiModelProperty("解析规则映射关系ID")
    private String ruleRelaId;

    /**
     * 资产名称
     */
    @ApiModelProperty("资产名称")
    private String assetName;

    /**
     * 日志分析规则ID
     */
    @ApiModelProperty("日志分析规则ID")
    private String analysisRuleId;

    /**
     * 日志分析规则名称
     */
    @ApiModelProperty("日志分析规则名称")
    private String analysisRuleName;

    /**
     * 安全等级
     */
    @ApiModelProperty("安全等级")
    private String severity;

    /**
     * 子系统
     */
    @ApiModelProperty("子系统")
    private String facility;

    /**
     * 解析类型
     */
    @ApiModelProperty("解析类型")
    private Integer ruleType;

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

    /**
     * 定制化解析方法所在Jar包
     */
    @ApiModelProperty("定制化解析方法所在Jar包")
    private String jarName;

    /**
     * Jar包版本
     */
    @ApiModelProperty("Jar包版本")
    private String version;

    /**
     * 方法名
     */
    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("归并字段列表")
    private String mergeItems;

    @ApiModelProperty("归并窗口时间")
    private Integer mergeWindowTime;

    @ApiModelProperty("事件窗口时间")
    private Integer eventWindowTime;

    @ApiModelProperty("事件判定阈值")
    private Integer eventThreshold;

    @ApiModelProperty("事件判定关键字或正则表达式")
    private String eventKeyword;

}
