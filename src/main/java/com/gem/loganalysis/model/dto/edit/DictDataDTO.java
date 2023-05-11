package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("系统配置 - 数据字典数据创建/更新 DTO ")
@Data
public class DictDataDTO {

    @ApiModelProperty(value = "字典编码", example = "1024")
    private Integer dataId;

    @ApiModelProperty(value = "显示顺序不能为空", example = "1024",required = true)
    @NotNull(message = "字典排序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "字典标签", example = "男",required = true)
    @NotNull(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    private String label;

    @ApiModelProperty(value = "字典值", required = true,example = "123")
    @Size(max = 100, message = "字典键值长度不能超过100个字符")
    @NotNull(message = "字典值不能为空")
    private String value;

    @ApiModelProperty(value = "字典类型", required = true ,example = "sys_common_sex")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    @NotNull(message = "字典类型不能为空")
    private String dictType;

    @ApiModelProperty(value = "状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty(value = "颜色类型", example = "default", notes = "default、primary、success、info、warning、danger")
    private String colorType;

    @ApiModelProperty(value = "css 样式", example = "btn-visible")
    private String cssClass;

    @ApiModelProperty(value = "备注", example = "我是一个角色")
    private String remark;

}
