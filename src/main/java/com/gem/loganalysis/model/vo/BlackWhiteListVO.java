package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 黑名单
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@ApiModel("安全管理 - 黑白名单 VO ")
public class BlackWhiteListVO {

    @ApiModelProperty(value = "组织机构唯一编码")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "关联资产唯一编码（*表示当前组织机构【含下属】的所有资产）")
    private String assetId;

    @ApiModelProperty(value = "关联资产名称")
    private String assetName;

    @ApiModelProperty(value = "资产IP地址")
    private String assetIp;

    @ApiModelProperty(value = "列入黑名单的IP")
    private String ipAddress;

    @ApiModelProperty(value = "有效时间，为空时表示永久有效,14位格式")
    private String validTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

}
