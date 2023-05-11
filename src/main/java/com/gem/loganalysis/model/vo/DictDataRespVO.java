package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/5 9:11
 */
@ApiModel("系统配置 - 数据字典数据VO Response ")
@Data
public class DictDataRespVO {

    @ApiModelProperty(value = "字典编码")
    private Integer dataId;

    @ApiModelProperty(value = "显示顺序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty(value = "颜色类型")
    private String colorType;

    @ApiModelProperty(value = "css 样式")
    private String cssClass;

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
