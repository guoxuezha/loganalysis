package com.gem.loganalysis.model.dto.edit;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 21:58
 */
@Data
public class BlockRuleDTO {

    /**
     * 封堵规则ID
     */
    private Integer blockOffRuleId;

    /**
     * 设备唯一编码
     */
    private String equipId;

    /**
     * 日志级别
     */
    private String severity;

    /**
     * 日志生产子系统
     */
    private String facility;

    /**
     * 来源IP字段名
     */
    private String ipItemName;

    /**
     * 封堵时长(秒)
     */
    private Long blockOffTime;

}
