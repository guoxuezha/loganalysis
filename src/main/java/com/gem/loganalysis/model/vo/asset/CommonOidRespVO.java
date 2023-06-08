package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("MIB库OID RespVO ")
@Data
public class CommonOidRespVO {

    @ApiModelProperty(value = "MIB版本不能为空")
    private String mibVersion;

    @ApiModelProperty(value = "OID")
    private String oid;

    @ApiModelProperty(value = "OID名称")
    private String oidName;

    @ApiModelProperty(value = "对应指标名称")
    private String measureName;

    @ApiModelProperty(value = "数据类型")
    private String oidDatatype;

    @ApiModelProperty(value = "OID说明")
    private String oidDesc;

}
