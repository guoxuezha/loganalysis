package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 封堵历史记录
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlockOffRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 封堵规则ID
     */
    @TableId("BLOCK_OFF_RULE_ID")
    private Integer blockOffRuleId;

    /**
     * 封堵IP
     */
    @TableField("BLOCK_OFF_IP")
    private String blockOffIp;

    /**
     * 封堵开始时间
     */
    @TableField("BLOCK_START_TIME")
    private String blockStartTime;

    /**
     * 封堵结束时间
     */
    @TableField("BLOCK_END_TIME")
    private String blockEndTime;

    /**
     * 封堵状态 1封堵中 2已解封
     */
    @TableField("BLOCK_STATE")
    private Integer blockState;

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
