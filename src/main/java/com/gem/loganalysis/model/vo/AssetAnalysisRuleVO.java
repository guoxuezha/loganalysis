package com.gem.loganalysis.model.vo;

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
    private Integer assetId;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 安全等级
     */
    private String severity;

    /**
     * 子系统
     */
    private String facility;

    /**
     * 解析类型
     */
    private Integer ruleType;

    /**
     * 分段字符
     */
    private String itemSplit;

    /**
     * 分值字符
     */
    private String kvSplit;

    /**
     * 定制化解析方法所在Jar包
     */
    private String jarName;

    /**
     * Jar包版本
     */
    private String version;

    /**
     * 方法名
     */
    private String methodName;


}
