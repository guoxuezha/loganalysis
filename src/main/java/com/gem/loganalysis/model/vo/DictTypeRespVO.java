package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("系统配置 - 数据字典类型VO Response ")
@Data
public class DictTypeRespVO {

    @ApiModelProperty(value = "字典主键")
    private Integer typeId;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

}
