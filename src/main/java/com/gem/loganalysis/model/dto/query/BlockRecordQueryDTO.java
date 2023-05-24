package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 22:05
 */
@Data
public class BlockRecordQueryDTO {

    /**
     * 记录类型(1封堵/2解封)
     */
    @ApiModelProperty("记录类型(1封堵/2解封)")
    private Integer recordType;

    /**
     * 资产ID
     */
    @ApiModelProperty("资产ID")
    private Integer assetId;

    /**
     * 封堵类型（0临时封堵/1永久封堵）
     */
    @ApiModelProperty("封堵类型（0临时封堵/1永久封堵）")
    private Integer blockType;

    /**
     * 封堵模式（0自动/1手动）
     */
    @ApiModelProperty("封堵模式（0自动/1手动）")
    private Integer blockMode;

    /**
     * 被封堵的IP地址
     */
    @ApiModelProperty("被封堵的IP地址")
    private String blockOffIp;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
