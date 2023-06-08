package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("SNMP参数设置删除/查询单个(联合主键部门和IP定位) DTO ")
@Data
public class AssetSnmpConfigDeleteDTO {

    @ApiModelProperty(value = "部门ID"
            , example = "1",required = true)
    @NotBlank(message = "部门ID不能为空")
    private String assetOrg;

    @ApiModelProperty(value = "IP地址(如非缺省端口则需带端口号，如172.168.1.1:10161)"
            , example = "172.168.1.1",required = true)
    @NotBlank(message = "IP地址不能为空")
    private String ipAddress;


}
