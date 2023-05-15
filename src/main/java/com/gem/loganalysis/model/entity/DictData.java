package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author czw
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SYSTEM_DICT_DATA")
public class DictData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(value = "DATA_ID", type = IdType.AUTO)
    private Integer dataId;

    /**
     * 字典排序
     */
    @TableField("SORT")
    private Integer sort;

    /**
     * 字典标签
     */
    @TableField("LABEL")
    private String label;

    /**
     * 字典键值
     */
    @TableField("VALUE")
    private String value;

    /**
     * 字典类型
     */
    @TableField("DICT_TYPE")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 颜色类型
     */
    @TableField("COLOR_TYPE")
    private String colorType;

    /**
     * css 样式
     */
    @TableField("CSS_CLASS")
    private String cssClass;

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
