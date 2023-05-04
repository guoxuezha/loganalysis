package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志解析规则
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogAnalysisRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "ANALYSIS_RULE_ID", type = IdType.AUTO)
    private Integer analysisRuleId;

    /**
     * 规则类型(1配置类规则 2硬编码规则)
     */
    @TableField("RULE_TYPE")
    private Integer ruleType;

    /**
     * 硬编码方法名
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 分段字符
     */
    @TableField("ITEM_SPLIT")
    private String itemSplit;

    /**
     * 分值字符
     */
    @TableField("KV_SPLIT")
    private String kvSplit;

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

    public LogAnalysisRule(LogAnalysisRuleDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.createTime = DateUtil.now();
        this.updateTime = DateUtil.now();
    }


}
