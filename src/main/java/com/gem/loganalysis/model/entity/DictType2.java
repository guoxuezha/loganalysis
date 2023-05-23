package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * 字典类型实体类
 *
 * @author huty
 * @since 2022-11-28
 */
@Data
@TableName("sys_dict_type")
@NoArgsConstructor
@AllArgsConstructor
public class DictType2 {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "字典主键")
    private Integer id;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

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
