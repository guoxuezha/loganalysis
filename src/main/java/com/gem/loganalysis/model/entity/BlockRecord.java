package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("封堵规则ID")
    private String blockRecordId;

    /**
     * 封堵IP
     */
    @TableField("BLOCK_IP")
    @ApiModelProperty("封堵IP")
    private String blockIp;

    /**
     * 封堵类型（0临时封堵/1永久封堵）
     */
    @TableField("BLOCK_TYPE")
    @ApiModelProperty("封堵类型（0临时封堵/1永久封堵）")
    private Integer blockType;

    /**
     * 封堵模式（0自动/1手动）
     */
    @TableField("BLOCK_MODE")
    @ApiModelProperty("封堵模式（0自动/1手动）")
    private Integer blockMode;

    /**
     * 封堵操作人
     */
    @TableField("BLOCK_OPERATOR")
    @ApiModelProperty("封堵操作人")
    private String blockOperator;

    /**
     * 封堵开始时间
     */
    @TableField("BLOCK_BEGIN_TIME")
    @ApiModelProperty("封堵开始时间")
    private String blockBeginTime;

    /**
     * 封堵结束时间
     */
    @TableField("BLOCK_END_TIME")
    @ApiModelProperty("封堵结束时间")
    private String blockEndTime;

    /**
     * 封堵状态（1封堵中/2已解封）
     */
    @TableField("BLOCK_STATE")
    @ApiModelProperty("封堵状态（1封堵中/2已解封）")
    private Integer blockState;

    /**
     * 执行封堵操作的资产编码
     */
    @TableField("ASSET_ID")
    @ApiModelProperty("执行封堵操作的资产编码")
    private String assetId;

    /**
     * 关联事件唯一编码
     */
    @TableField("EVENT_ID")
    @ApiModelProperty("关联事件唯一编码")
    private String eventId;

}
