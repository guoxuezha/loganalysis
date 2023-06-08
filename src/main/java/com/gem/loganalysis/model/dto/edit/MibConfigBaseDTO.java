package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("MIB指令集设置基础 DTO ")
@Data
public class MibConfigBaseDTO {

    @ApiModelProperty(value = "MIB版本", example = "COMMON1",required = true)
    @NotBlank(message = "MIB版本不能为空")
    private String mibVersion;

    @ApiModelProperty(value = "OID",example = "1.3.6.1.2.1.25.2.3.1",required = true)
    @NotBlank(message = "SNMP命令要发送的OID不能为空")
    private String oid;

}
