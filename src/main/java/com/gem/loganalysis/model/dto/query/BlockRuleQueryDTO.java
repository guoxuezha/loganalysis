package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 21:48
 */
@Data
public class BlockRuleQueryDTO {

    /**
     * 设备唯一编码
     */
    private Integer equipId;

    /**
     * 日志级别
     */
    private String severity;

    /**
     * 日志生产子系统
     */
    private String facility;

}
