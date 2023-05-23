package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sop_block_record")
public class BlockRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 封堵规则ID
     */
    @TableId("BLOCK_RECORD_ID")
    private String blockRecordId;

    /**
     * 封堵IP
     */
    @TableField("BLOCK_IP")
    private String blockIp;

    /**
     * 封堵类型（0临时封堵/1永久封堵）
     */
    @TableField("BLOCK_TYPE")
    private Integer blockType;

    /**
     * 封堵模式（0自动/1手动）
     */
    @TableField("BLOCK_MODE")
    private Integer blockMode;

    /**
     * 封堵操作人
     */
    @TableField("BLOCK_OPERATOR")
    private String blockOperator;

    /**
     * 封堵开始时间
     */
    @TableField("BLOCK_BEGIN_TIME")
    private String blockBeginTime;

    /**
     * 封堵结束时间
     */
    @TableField("BLOCK_END_TIME")
    private String blockEndTime;

    /**
     * 封堵状态（1封堵中/2已解封）
     */
    @TableField("BLOCK_STATE")
    private Integer blockState;

    /**
     * 执行封堵操作的资产编码
     */
    @TableField("ASSET_ID")
    private String assetId;

    /**
     * 关联事件唯一编码
     */
    @TableField("EVENT_ID")
    private String eventId;

}
