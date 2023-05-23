package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 字典数据 vo类
 *
 * @author czw
 * @since 2023-02-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictItemRespVO {

    @ApiModelProperty(value = "字典项主键")
    private Integer id;

    @ApiModelProperty(value = "字典项排序")
    private Integer sort;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典项内容")
    private String text;

    @ApiModelProperty(value = "层级(0为第一层)")
    private Integer level;

    @ApiModelProperty(value = "字典项键值")
    private String value;

    @ApiModelProperty(value = "字典类型ID")
    private Integer typeId;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty(value = "父项ID")
    private Integer parentId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "子项数据")
    private List<DictItemRespVO> children;


}
