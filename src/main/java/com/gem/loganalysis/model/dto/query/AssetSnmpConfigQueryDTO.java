package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("SNMP参数设置查询 DTO ")
@Data
public class AssetSnmpConfigQueryDTO {

    @ApiModelProperty(value = "部门ID精确查询")
    private String assetOrg;

    @ApiModelProperty(value = "IP地址(模糊查询)")
    private String ipAddress;

    @ApiModelProperty(value = "SNMP版本(模糊查询)")
    private String snmpVersion;

}
