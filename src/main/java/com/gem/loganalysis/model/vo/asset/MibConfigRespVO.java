package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("MIB库指令设置 RespVO ")
@Data
public class MibConfigRespVO {

    @ApiModelProperty(value = "OID")
    private String oid;

    @ApiModelProperty(value = "MIB版本")
    private String mibVersion;

    @ApiModelProperty(value = "命令发送方式(SNMPGET/SNMPWALK)")
    private String snmpMethod;

    @ApiModelProperty(value = "对应指标分类")
    private String measureType;

}
