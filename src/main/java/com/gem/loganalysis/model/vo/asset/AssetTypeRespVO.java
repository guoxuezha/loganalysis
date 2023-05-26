package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 资产类型关联数据VO Response ")
@Data
public class AssetTypeRespVO {

    @ApiModelProperty(value = "品牌列表")
    private List<String> assetBrandList;

    @ApiModelProperty(value = "型号列表")
    private List<String> assetModelList;

    @ApiModelProperty(value = "操作系统版本列表")
    private List<String> osVersionList;


}
