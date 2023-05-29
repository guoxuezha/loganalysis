package com.gem.loganalysis.model.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("资产模块 - 资产类型VO Response ")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetTypeVO {

    @ApiModelProperty(value = "资产类别")
    private String assetType;

    @ApiModelProperty(value = "资产类别关联数据")
    private List<AssetTypeRespVO> list;

}
