package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("资产模块 - 安全管理资产更新 DTO ")
@Data
public class AssetUpdateDTO {

    @ApiModelProperty(value = "资产唯一编码",required = true)
    @NotNull(message = "资产唯一编码不能为空")
    private String assetId;

    @ApiModelProperty(value = "资产管理人")
    private String assetManager;

    @ApiModelProperty(value = "资产分组")
    private String assetGroupId;

    @ApiModelProperty(value = "资产标签")
    private String assetTag;


}
