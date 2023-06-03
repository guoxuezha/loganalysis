package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.BlockRuleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@TableName("sop_block_rule")
public class BlockRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 封堵规则ID
     */
    @TableId(value = "BLOCK_RULE_ID", type = IdType.AUTO)
    private String blockRuleId;

    /**
     * 封堵规则描述
     */
    @TableField("BLOCK_RULE_DESC")
    private String blockRuleDesc;

    /**
     * 封堵类型（0临时封堵/1永久封堵）
     */
    @TableField("BLOCK_TYPE")
    private Integer blockType;

    /**
     * 临时封堵时长（分钟）
     */
    @TableField("BLOCK_DURATION")
    private Integer blockDuration;

    /**
     * 启用白名单（0否1是）
     */
    @TableField("WHITE_LIST_ENABLE")
    private Integer whiteListEnable;

    /**
     * 启用黑名单（0否1是）
     */
    @TableField("BLACK_LIST_ENABLE")
    private Integer blackListEnable;

    /**
     * 执行操作的资产(防火墙)ID(若有多个则使用,分割)
     */
    @TableField("OPERATION_ASSET_ID")
    private String operationAssetId;

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

    public BlockRule(BlockRuleDTO dto) {
        BeanUtil.copyProperties(dto, this);
        if (StrUtil.isNotEmpty(dto.getBlockRuleId())) {
            this.updateTime = DateUtil.today();
        } else {
            this.createTime = DateUtil.today();
        }
    }


}
