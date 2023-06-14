package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("资产模块 - 逻辑资产扫描查询 DTO ")
@Data
public class LogicalAssetQueryDTO {

    @ApiModelProperty(value = "资产所属组织(部门)")
    private String assetOrg;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "可用端口号")
    private String enablePort;

    @ApiModelProperty(value = "资产扫描批次开始时间")
    private String beginScanTime;

    @ApiModelProperty(value = "资产扫描批次结束时间")
    private String endScanTime;

}
