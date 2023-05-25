package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("资产模块 - 物理资产扫描 DTO ")
@Data
public class PhysicalAssetQueryDTO {

    @ApiModelProperty(value = "资产所属组织(部门)")
    private String assetOrg;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "资产状态(0无1有)")
    private Integer assetStatus;

    @ApiModelProperty(value = "资产扫描批次开始时间")
    private String beginScanTime;

    @ApiModelProperty(value = "资产扫描批次结束时间")
    private String endScanTime;

}
