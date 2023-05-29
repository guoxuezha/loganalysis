package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 资产类型查询 DTO ")
@Data
public class AssetTypeQueryDTO {

    @ApiModelProperty(value = "资产类别")
    private String assetType;

}
