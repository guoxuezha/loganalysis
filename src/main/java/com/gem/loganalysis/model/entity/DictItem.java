package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * 字典项实体类
 *
 * @author huty
 * @since 2022-11-28
 */
@Data
@TableName("sys_dict_item")
@NoArgsConstructor
@AllArgsConstructor
public class DictItem {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "字典项主键")
    private Integer id;

    @ApiModelProperty(value = "字典项排序")
    private Integer sort;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "字典项内容")
    private String text;

    @ApiModelProperty(value = "字典项键值")
    private String value;

    @ApiModelProperty(value = "字典类型ID")
    private Integer typeId;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "父项ID")
    private Integer parentId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    @TableLogic
    @ApiModelProperty(value = "是否删除（0否 1是）")
    private Integer deleteState;
}
