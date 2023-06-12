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
 * <p>
 * 资产风险记录
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("sop_asset_risk")
public class AssetRisk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资产唯一编码
     */
    @TableId("ASSET_ID")
    private String assetId;

    /**
     * 漏洞编码
     */
    @TableField("VULN_ID")
    private String vulnId;

    /**
     * 扫描批次时间
     */
    @TableField("SCAN_TIME")
    private String scanTime;

    /**
     * 风险告警级别(1/2/3 低危/中危/高危)
     */
    @TableField("RISK_LEVEL")
    private Integer riskLevel;

    /**
     * 关联事件唯一编码
     */
    @TableField("REF_EVENT_ID")
    private String refEventId;

    /**
     * 处置状态，产生事件的情况下与EVENT处置状态同步（0待处理/1处理中/2处理完成/3忽略）
     */
    @TableField("HANDLE_STATUS")
    private Integer handleStatus;

    /**
     * 处置状态更新时间
     */
    @TableField("STATUS_CHANGE_TIME")
    private String statusChangeTime;

}
