package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统配置 - 字典数据查询 DTO ")
@Data
public class DictDataQueryDTO {

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态(0正常 1停用)")
    private Integer status;

}
