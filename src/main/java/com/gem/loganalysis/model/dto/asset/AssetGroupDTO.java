package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("管理后台 - 资产分组查询 DTO ")
@Data
public class AssetGroupDTO {

    @ApiModelProperty(value = "资产所属组织（部门）")
    private String assetOrg;

    @ApiModelProperty(value = "资产分组编码")
    private String groupId;

    @ApiModelProperty(value = "资产分组名称")
    private String groupName;

    @ApiModelProperty(value = "资产分组说明")
    private String groupDesc;

}
