package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("通用MIB库OID查询 DTO ")
@Data
public class CommonOidQueryDTO {

    @ApiModelProperty(value = "OID(模糊查询)")
    private String oid;

    @ApiModelProperty(value = "OID名称(模糊查询)")
    private String oidName;

    @ApiModelProperty(value = "MIB版本(模糊查询)")
    private String mibVersion;

    @ApiModelProperty(value = "对应指标名称(模糊查询)")
    private String measureName;

}
