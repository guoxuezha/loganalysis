package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("SNMP参数设置 RespVO ")
@Data
public class AssetSnmpConfigRespVO {

    @ApiModelProperty(value = "部门ID")
    private String assetOrg;

    @ApiModelProperty(value = "部门名称")
    private String assetOrgName;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "SNMP版本不能为空)")
    private String snmpVersion;

    @ApiModelProperty(value = "SNMP-V1/2c COMMUNITY")
    private String snmpCommunity;

    @ApiModelProperty(value = "SNMP-V3 USERNAME")
    private String snmpUsername;

    @ApiModelProperty(value = "AUTH_PASSWORD")
    private String authPassword;

    @ApiModelProperty(value = "SNMP-V3 PRIV_PASSWORD")
    private String privPassword;

    @ApiModelProperty(value = "OUTPUT_PARAM")
    private String outputParam;

    @ApiModelProperty(value = "关联资产编码")
    private String assetId;

    @ApiModelProperty(value = "关联资产名称")
    private String assetName;

}
