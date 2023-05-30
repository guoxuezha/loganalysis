package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("资产模块 - 逻辑资产扫描查询 DTO ")
@Data
public class LogicalDiscoveryQueryDTO {

    @ApiModelProperty(value = "端口号(精确查询)")
    private Integer port;

    @ApiModelProperty(value = "网络协议(精确查询)")
    private String networkProtocol;

    @ApiModelProperty(value = "资产类型(模糊匹配)")
    private String assetType;


}
