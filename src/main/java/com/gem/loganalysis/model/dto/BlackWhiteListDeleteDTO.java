package com.gem.loganalysis.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
@ApiModel("安全管理 - 黑白名单删除 DTO ")
public class BlackWhiteListDeleteDTO {

    @ApiModelProperty(value = "组织机构唯一编码",required = true)
    @NotBlank(message = "请传入组织机构唯一编码")
    private String orgId;

    @ApiModelProperty(value = "关联资产唯一编码（*表示当前组织机构【含下属】的所有资产）",required = true)
    @NotBlank(message = "请传入关联资产唯一编码,*表示当前组织机构【含下属】的所有资产")
    private String assetId;

    @ApiModelProperty(value = "列入黑名单的IP",required = true)
    @NotBlank(message = "请传入列入黑名单的IP")
    private String ipAddress;

}
