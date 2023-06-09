package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("资产模块 - 物理IP资产扫描VO Response ")
@Data
public class
PhysicalAssetScannerRespVO {

    @ApiModelProperty(value = "唯一编码")
    private String recordId;

    @ApiModelProperty(value = "资产所属组织(部门)")
    private String assetOrg;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "资产状态(0无1有)")
    private Integer assetStatus;

    @ApiModelProperty(value = "资产扫描批次时间")
    private String scanTime;

    @ApiModelProperty(value = "是否存在(纳管) 0为纳管(已存在) 1为未纳管(不存在)")
    private Integer existsInAssetTable;

    @ApiModelProperty(value = "扫描类型" +
            "(0为自动 1为手动)")
    private String type;

}
