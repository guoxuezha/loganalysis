package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 23:05
 */
@Data
public class AssetEventVO {

    @ApiModelProperty(value = "事件唯一编码")
    private String eventId;

    @ApiModelProperty(value = "事件关联资产编码")
    private String assetId;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "事件起源（0未知/1日志/2扫描/3漏洞库）")
    private Integer eventOrigin;

    @ApiModelProperty(value = "事件起源信息编码（与EVENT_ORIGIN联动，1时为LOG_ID）")
    private String originId;

    @ApiModelProperty(value = "事件类型")
    private String eventType;

    @ApiModelProperty(value = "事件等级")
    private String eventClass;

    @ApiModelProperty(value = "源端IP")
    private String sourceIp;

    @ApiModelProperty(value = "事件起始时间")
    private String beginTime;

    @ApiModelProperty(value = "事件终止时间")
    private String endTime;

    @ApiModelProperty(value = "事件推送消息内容")
    private String eventMessage;

    @ApiModelProperty(value = "处置状态（0待处理/1处理中/2处理完成/3忽略）")
    private Integer handleStatus;

}
