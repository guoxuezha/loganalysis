package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志解析规则映射关系
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogAnalysisRuleRela implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联关系ID
     */
    @TableField("RULE_RELA_ID")
    private Integer ruleRelaId;

    /**
     * 设备唯一编码
     */
    @TableId("EQUIP_ID")
    private Integer equipId;

    /**
     * 日志级别
     */
    @TableField("SEVERITY")
    private String severity;

    /**
     * 日志生产子系统
     */
    @TableField("FACILITY")
    private String facility;

    /**
     * 规则ID
     */
    @TableField("ANALYSIS_RULE_ID")
    private Integer analysisRuleId;

    /**
     * 归并字段(列表)
     */
    @TableField("MERGE_ITEMS")
    private String mergeItems;

    /**
     * 归并窗口时长 用于判断是否需要执行归并
     */
    @TableField("WINDOW_TIME")
    private Long windowTime;

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
