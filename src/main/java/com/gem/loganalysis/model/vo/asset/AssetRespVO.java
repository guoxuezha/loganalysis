package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("资产模块 - 安全资产VO Response ")
@Data
public class AssetRespVO {

    @ApiModelProperty(value = "资产唯一编码")
    private String assetId;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产分类，物理资产/逻辑资产")
    private String assetClass;

    @ApiModelProperty(value = "资产分类(名称)")
    private String assetClassName;

    @ApiModelProperty(value = "资产类型ID，服务器/路由器/交换机/防火墙或者数据库/中间件/网络应用/域名等")
    private String assetType;

    @ApiModelProperty(value = "资产类别(大类别名称)")
    private String assetCategory;

    @ApiModelProperty(value = "资产类别(小类别名称)")
    private String typeName;

    @ApiModelProperty(value = "资产类型(全称)")
    private String assetTypeName;

    @ApiModelProperty(value = "资产描述")
    private String assetDesc;

    @ApiModelProperty(value = "品牌")
    private String assetBrand;

    @ApiModelProperty(value = "型号")
    private String assetModel;

    @ApiModelProperty(value = "规格")
    private String assetSpec;

    @ApiModelProperty(value = "操作系统版本")
    private String osVersion;

    @ApiModelProperty(value = "固件版本")
    private String firmwareVersion;

    @ApiModelProperty(value = "软件版本")
    private String softwareVersion;

    @ApiModelProperty(value = "MIB版本")
    private String mibVersion;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "服务端口")
    private Integer servicePort;

    @ApiModelProperty(value = "网管端口")
    private Integer nmPort;

    @ApiModelProperty(value = "网管协议")
    private String nmProcotol;

    @ApiModelProperty(value = "网管账号")
    private String nmAccount;

    @ApiModelProperty(value = "资产所属组织（部门）")
    private String assetOrg;

    @ApiModelProperty(value = "资产所属组织（部门）名称")
    private String assetOrgName;

    @ApiModelProperty(value = "资产管理人ID")
    private String assetManager;

    @ApiModelProperty(value = "资产管理人名称")
    private String assetManagerName;

    @ApiModelProperty(value = "资产管理人电话")
    private String assetManagerMobile;

    @ApiModelProperty(value = "资产分组")
    private String assetGroupId;

    @ApiModelProperty(value = "资产分组名称")
    private String assetGroupName;

    @ApiModelProperty(value = "资产标签")
    private String assetTag;

    @ApiModelProperty(value = "资产状态（0在役/1退役/2在线/3离线）")
    private String assetStatus;

    @ApiModelProperty(value = "资产状态（0在役/1退役/2在线/3离线）")
    private String assetStatusName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "资产安全状态（0危险/1告警/2正常/3离线）")
    private String assetSecurityStatus;

    @ApiModelProperty(value = "资产安全状态(名称)")
    private String assetSecurityStatusName;

    @ApiModelProperty(value = "分数")
    private Double score;

    @ApiModelProperty(value = "脆弱值")
    private Double severity;

}
