package com.gem.loganalysis.model.dto.query;

import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/22 15:01
 */
@Data
public class EventQueryDTO {

    /**
     * 资产ID
     */
    private String assetId;

    /**
     * 事件起源类型
     */
    private Integer eventOrigin;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件等级
     */
    private String eventClass;

    /**
     * 源端IP
     */
    private String sourceIp;

    /**
     * 事件起始时间
     */
    private String beginTime;

    /**
     * 事件终止时间
     */
    private String endTime;

}
