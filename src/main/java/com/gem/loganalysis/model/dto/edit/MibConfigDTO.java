package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ApiModel("MIB指令集设置新增/变更 DTO ")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MibConfigDTO extends MibConfigBaseDTO {

    @ApiModelProperty(value = "命令发送方式", example = "SNMPGET",required = true)
    @NotBlank(message = "命令发送方式不能为空")
    private String snmpMethod;

    @ApiModelProperty(value = "对应指标分类名称",required = true)
    @NotBlank(message = "对应指标分类不能为空")
    private String measureType;

}
