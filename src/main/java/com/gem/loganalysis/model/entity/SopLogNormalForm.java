package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 日志范式
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_log_normal_form")
public class SopLogNormalForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志项ID
     */
    @TableId("FIELD_ID")
    private String fieldId;

    /**
     * 日志项名称(Key)
     */
    @TableField("FIELD_NAME")
    private String fieldName;

    /**
     * 日志项描述(中文)
     */
    @TableField("FIELD_DESC")
    private String fieldDesc;

    /**
     * 父节点ID
     */
    @TableField("PID")
    private String pid;

    /**
     * 所属层级
     */
    @TableField("LEVEL")
    private Integer level;

    /**
     * 属性标签 (1/2/3/4 源IP/目标IP/事件类型/危险等级)
     */
    @TableField("FIELD_TAG")
    private Integer fieldTag;

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
