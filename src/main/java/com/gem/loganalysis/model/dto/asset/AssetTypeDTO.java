package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 资产类型新增/编辑 DTO ")
@Data
public class AssetTypeDTO {

    @ApiModelProperty(value = "类型ID")
    private Integer typeId;

    @ApiModelProperty(value = "资产类别")
    private String assetType;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "品牌列表")
    private List<String> assetBrandList;

    @ApiModelProperty(value = "型号列表")
    private List<String> assetModelList;

    @ApiModelProperty(value = "操作系统版本列表")
    private List<String> osVersionList;

}
