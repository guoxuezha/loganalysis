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

    @ApiModelProperty(value = "类别ID,目前类别没ID因为是一张表存的，怕前端显示不了先返一个0")
    private Integer typeId;

    @ApiModelProperty(value = "资产类别")
    private String typeName;

    @ApiModelProperty(value = "资产类别关联数据")
    private List<AssetTypeRespVO> list;

}
