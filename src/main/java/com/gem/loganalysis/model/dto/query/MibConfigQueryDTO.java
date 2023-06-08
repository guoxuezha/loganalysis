package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("MIB指令设置查询 DTO ")
@Data
public class MibConfigQueryDTO {

    @ApiModelProperty(value = "OID(模糊查询)")
    private String oid;

    @ApiModelProperty(value = "MIB版本(模糊查询)")
    private String mibVersion;

    @ApiModelProperty(value = "命令发送方式(SNMPGET/SNMPWALK)")
    private String snmpMethod;

    @ApiModelProperty(value = "对应指标分类(模糊查询)")
    private String measureType;

}
