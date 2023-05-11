package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("系统配置 - 数据字典类型创建/更新 DTO ")
@Data
public class DictTypeDTO {

    @ApiModelProperty(value = "字典主键")
    private Integer typeId;

    @ApiModelProperty(value = "字典名称",required = true)
    @NotNull(message = "请传入字典类型名称")
    private String name;

    @ApiModelProperty(value = "字典类型",required = true)
    @NotNull(message = "请传入字典类型")
    private String type;

    @ApiModelProperty(value = "状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

}
