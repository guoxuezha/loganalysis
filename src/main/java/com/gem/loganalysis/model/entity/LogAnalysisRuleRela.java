package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleRelaDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志解析规则映射关系
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_log_analysis_rule_rela")
@NoArgsConstructor
public class LogAnalysisRuleRela implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联关系编码
     */
    @TableId("RULE_RELA_ID")
    private String ruleRelaId;

    /**
     * 资产唯一编码
     */
    @TableField("ASSET_ID")
    private String assetId;

    /**
     * 日志级别
     */
    @TableField("SEVERITY")
    private String severity;

    /**
     * 产生日志的子系统
     */
    @TableField("FACILITY")
    private String facility;

    /**
     * 日志分析规则编码
     */
    @TableField("ANALYSIS_RULE_ID")
    private String analysisRuleId;

    /**
     * 归并字段属性列表
     */
    @TableField("MERGE_ITEMS")
    private String mergeItems;

    /**
     * 归并窗口时长（分钟）
     */
    @TableField("MERGE_WINDOW_TIME")
    private Integer mergeWindowTime;

    /**
     * 事件判定窗口时长（分钟）
     */
    @TableField("EVENT_WINDOW_TIME")
    private Integer eventWindowTime;

    /**
     * 事件判定阈值
     */
    @TableField("EVENT_THRESHOLD")
    private Integer eventThreshold;

    /**
     * 事件判定关键字或正则表达式
     */
    @TableField("EVENT_KEYWORD")
    private String eventKeyword;

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

    public LogAnalysisRuleRela(LogAnalysisRuleRelaDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.createTime = this.updateTime = DateUtil.now();
        //DateUtil.format(new Date(), PURE_DATETIME_FORMAT);
    }

}
