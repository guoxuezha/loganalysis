package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("资产模块 - 资产分组VO Response ")
@Data
public class AssetGroupRespVO {

    @ApiModelProperty(value = "资产所属组织（部门）")
    private String assetOrg;

    @ApiModelProperty(value = "资产所属组织（部门）名称")
    private String assetOrgName;

    @ApiModelProperty(value = "资产分组编码")
    private String groupId;

    @ApiModelProperty(value = "资产分组名称")
    private String groupName;

    @ApiModelProperty(value = "资产分组说明")
    private String groupDesc;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

}
