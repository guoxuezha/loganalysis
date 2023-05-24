package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "资产ID")
    private String assetId;

    /**
     * 事件处置状态
     */
    @ApiModelProperty(value = "事件处置状态")
    private Integer handleStatus;

    /**
     * 事件起源类型
     */
    @ApiModelProperty(value = "事件起源类型")
    private Integer eventOrigin;

    /**
     * 事件类型
     */
    @ApiModelProperty(value = "事件类型")
    private String eventType;

    /**
     * 事件等级
     */
    @ApiModelProperty(value = "事件等级")
    private String eventClass;

    /**
     * 源端IP
     */
    @ApiModelProperty(value = "源端IP")
    private String sourceIp;

    /**
     * 事件起始时间
     */
    @ApiModelProperty(value = "事件起始时间")
    private String beginTime;

    /**
     * 事件终止时间
     */
    @ApiModelProperty(value = "事件终止时间")
    private String endTime;

}
