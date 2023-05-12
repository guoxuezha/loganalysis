package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("资产模块 - 逻辑资产扫描VO Response ")
@Data
public class LogicalAssetScannerRespVO {

    @ApiModelProperty(value = "唯一编码")
    private String recordId;

    @ApiModelProperty(value = "资产所属组织(部门)")
    private String assetOrg;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "可用端口号")
    private String enablePort;

    @ApiModelProperty(value = "推测的资产类型")
    private String assetType;

    @ApiModelProperty(value = "扫描获得的反馈信息")
    private String assetInfo;

    @ApiModelProperty(value = "资产扫描批次时间")
    private String scanTime;

}
