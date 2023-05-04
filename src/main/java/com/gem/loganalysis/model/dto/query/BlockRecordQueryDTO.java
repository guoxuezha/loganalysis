package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 22:05
 */
@Data
public class BlockRecordQueryDTO {

    /**
     * 安全设备ID
     */
    private Integer equipId;

    /**
     * 被封堵的IP地址
     */
    private String blockOffIp;

    /**
     * 封堵开始时间
     */
    private String blockStartTime;

    /**
     * 封堵结束时间
     */
    private String blockEndTime;

}
