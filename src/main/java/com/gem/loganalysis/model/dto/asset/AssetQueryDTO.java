package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("资产模块 - 安全管理资产查询 DTO ")
@Data
@Accessors(chain = true)//链式
public class AssetQueryDTO {

    @ApiModelProperty(value = "资产名称(模糊查询)")
    private String assetName;

    @ApiModelProperty(value = "资产分类，物理资产/逻辑资产")
    private String assetClass;

    @ApiModelProperty(value = "资产类型，服务器/路由器/交换机/防火墙或者数据库/中间件/网络应用/域名等")
    private String assetType;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "资产管理人(名称模糊匹配,不是ID)")
    private String assetManager;

    @ApiModelProperty(value = "资产管理人")
    private String assetManagerId;

    @ApiModelProperty(value = "资产分组(ID精确查询)")
    private String assetGroupId;

    @ApiModelProperty(value = "资产状态（0在役/1退役/2在线/3离线）")
    private String assetStatus;

    @ApiModelProperty(value = "资产部门(ID精确查询)")
    private String assetOrg;

    @ApiModelProperty(value = "资产标签")
    private String assetTag;

}
