package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 日志字段映射
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_log_field_mapping")
public class SopLogFieldMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志解析规则关联关系编码
     */
    @TableField("RULE_RELA_ID")
    private String ruleRelaId;

    /**
     * 源端日志项名称
     */
    @TableField("SOURCE_FIELD_NAME")
    private String sourceFieldName;

    /**
     * 源端日志项描述
     */
    @TableField("SOURCE_FIELD_DESC")
    private String sourceFieldDesc;

    /**
     * 目标日志项ID
     */
    @TableField("TARGET_FIELD_ID")
    private String targetFieldId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标记
     */
    @TableField("DELETE_STATE")
    private Integer deleteState;


}
