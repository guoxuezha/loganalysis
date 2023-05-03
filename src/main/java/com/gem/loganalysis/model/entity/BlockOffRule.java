package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 封堵规则
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlockOffRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 封堵规则ID
     */
    @TableField("BLOCK_OFF_RULE_ID")
    private Integer blockOffRuleId;

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
     * 来源IP字段名
     */
    @TableField("IP_ITEM_NAME")
    private String ipItemName;

    /**
     * 封堵时长(秒)
     */
    @TableField("BLOCK_OFF_TIME")
    private Long blockOffTime;

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
