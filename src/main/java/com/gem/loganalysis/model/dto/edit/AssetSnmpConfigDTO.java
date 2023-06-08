package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("SNMP参数设置新增 DTO ")
@Data
public class AssetSnmpConfigDTO{

    @ApiModelProperty(value = "IP地址(如非缺省端口则需带端口号，如172.168.1.1:10161)"
            , example = "172.168.1.1",required = true)
    @NotBlank(message = "IP地址不能为空")
    private String ipAddress;

    @ApiModelProperty(value = "SNMP版本不能为空)", example = "2c",required = true)
    @NotBlank(message = "SNMP版本不能为空")
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

}
