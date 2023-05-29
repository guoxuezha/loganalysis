package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("资产模块 - 逻辑资产扫描规则 VO ")
@Data
public class LogicalAssetDiscoveryRuleVO {

    @ApiModelProperty(value = "唯一编码")
    private String logicalAssetDiscoveryId;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "网络协议")
    private String networkProtocol;

    @ApiModelProperty(value = "资产类型")
    private String assetType;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


}
