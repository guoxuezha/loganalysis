package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("资产模块 - vlan设置查询 DTO ")
@Data
public class OrgVlanQueryDTO {

    @ApiModelProperty(value = "资产所属组织(部门)")
    private String orgId;

}
