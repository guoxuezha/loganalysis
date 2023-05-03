package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志索引
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
public class LogIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联关系ID
     */
    @TableId("RULE_RELA_ID")
    private Integer ruleRelaId;

    /**
     * 字段联合主键
     */
    @TableField("UNION_KEY")
    private String unionKey;

    /**
     * 日志内容ID
     */
    @TableField("LOG_ID")
    private Integer logId;


}
