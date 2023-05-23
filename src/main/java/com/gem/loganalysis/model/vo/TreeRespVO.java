package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("系统模块 - 通用树形 VO ")
@Data
public class TreeRespVO {

    @ApiModelProperty(value = "节点名称")
    private String label;

    @ApiModelProperty(value = "节点键值")
    private String value;

    @ApiModelProperty(value = "父节点ID")
    private String parentId;

    @ApiModelProperty(value = "子节点")
    private List<TreeRespVO> children;

}
