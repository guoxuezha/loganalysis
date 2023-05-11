package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统配置 - 字典类型查询 DTO ")
@Data
public class DictTypeQueryDTO {

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "状态(0正常 1停用)")
    private Integer status;

}
