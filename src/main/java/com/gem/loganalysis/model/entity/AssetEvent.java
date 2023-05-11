package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 资产事件记录
 *
 * @author GuoChao
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_asset_event")
@Builder
public class AssetEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件唯一编码
     */
    @TableId("EVENT_ID")
    private String eventId;

    /**
     * 事件关联资产编码
     */
    @TableField("ASSET_ID")
    private String assetId;

    /**
     * 事件起源（0未知/1日志/2扫描/3漏洞库）
     */
    @TableField("EVENT_ORIGIN")
    private Integer eventOrigin;

    /**
     * 事件起源信息编码（与EVENT_ORIGIN联动，1时为LOG_ID）
     */
    @TableField("ORIGIN_ID")
    private String originId;

    /**
     * 事件类型
     */
    @TableField("EVENT_TYPE")
    private String eventType;

    /**
     * 事件等级
     */
    @TableField("EVENT_CLASS")
    private String eventClass;

    /**
     * 源端IP
     */
    @TableField("SOURCE_IP")
    private String sourceIp;

    /**
     * 事件起始时间
     */
    @TableField("BEGIN_TIME")
    private String beginTime;

    /**
     * 事件终止时间
     */
    @TableField("END_TIME")
    private String endTime;

    /**
     * 事件推送消息内容
     */
    @TableField("EVENT_MESSAGE")
    private String eventMessage;

    /**
     * 处置状态（0待处理/1处理中/2处理完成/3忽略）
     */
    @TableField("HANDLE_STATUS")
    private Integer handleStatus;


}
