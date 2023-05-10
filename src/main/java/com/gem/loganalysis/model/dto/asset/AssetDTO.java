package com.gem.loganalysis.model.dto.asset;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

@ApiModel("管理后台 - 安全管理资产创建/更新 DTO ")
@Data
public class AssetDTO {

    @ApiModelProperty(value = "资产唯一编码")
    private String assetId;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产分类，物理资产/逻辑资产")
    private String assetClass;

    @ApiModelProperty(value = "资产类型，服务器/路由器/交换机/防火墙或者数据库/中间件/网络应用/域名等")
    private String assetType;

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

    @ApiModelProperty(value = "网管密码（加密存储）")
    private String nmPassword;

    @ApiModelProperty(value = "资产所属组织（部门）")
    private String assetOrg;

    @ApiModelProperty(value = "资产管理人")
    private String assetManager;

    @ApiModelProperty(value = "资产分组")
    private String assetGroupId;

    @ApiModelProperty(value = "资产标签")
    private String assetTag;

    @ApiModelProperty(value = "资产状态（0在役/1退役/2在线/3离线）")
    private String assetStatus;
}
