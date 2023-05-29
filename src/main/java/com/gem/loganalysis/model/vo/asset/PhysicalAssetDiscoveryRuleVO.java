package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 物理资产扫描规则 VO ")
@Data
public class PhysicalAssetDiscoveryRuleVO {

    @ApiModelProperty(value = "唯一编码")
    private String physicalAssetDiscoveryId;

    @ApiModelProperty(value = "community字符串")
    private String community;

    @ApiModelProperty(value = "多组OID")
    private List<String> oidList;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;



}
