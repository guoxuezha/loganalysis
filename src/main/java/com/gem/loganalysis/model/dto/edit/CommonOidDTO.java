package com.gem.loganalysis.model.dto.edit;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ApiModel("通用MIB库OID新增/变更 DTO ")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommonOidDTO  extends CommonOidBaseDTO {

    @ApiModelProperty(value = "OID名称", example = "hrTest",required = true)
    @NotBlank(message = "OID名称不能为空")
    private String oidName;

    @ApiModelProperty(value = "对应指标名称", example = "hrTest",required = true)
    @NotBlank(message = "对应指标名称不能为空")
    private String measureName;

    @ApiModelProperty(value = "数据类型", example = "STRING",required = true)
    @NotBlank(message = "数据类型不能为空")
    private String oidDatatype;

    @ApiModelProperty(value = "OID说明")
    private String oidDesc;
}
