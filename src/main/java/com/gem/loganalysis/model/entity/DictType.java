package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SYSTEM_DICT_TYPE")
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(value = "TYPE_ID", type = IdType.AUTO)
    private Integer typeId;

    /**
     * 字典名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 字典类型
     */
    @TableField("TYPE")
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateTime;
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;
    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    /**
     * 是否删除
     */
    @TableField("DELETE_STATE")
    @TableLogic
    private Integer deleteState;


}
