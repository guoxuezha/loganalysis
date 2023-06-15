package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.EventTypeInsertDTO;
import com.gem.loganalysis.model.dto.edit.EventTypeUpdateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 每日数据
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_daily_data")
@NoArgsConstructor
public class DailyData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 存储日期 yyyy-MM-dd格式
     */
    @TableId(value = "DATE_TIME",type = IdType.INPUT)
    private String dateTime;

    /**
     * 低风险个数
     */
    @TableField(value = "LOW_RISK_COUNT")
    private Integer lowRiskCount;

    /**
     * 中风险个数
     */
    @TableField(value = "MEDIUM_RISK_COUNT")
    private Integer mediumRiskCount;

    /**
     * 高风险个数
     */
    @TableField(value = "HIGH_RISK_COUNT")
    private Integer highRiskCount;

    /**
     * 出口设备负荷(流量)
     */
    @TableField(value = "EXPORT_DEVICE_LOAD")
    private Double exportDeviceLoad;

    /**
     * 安全资产评分
     */
    @TableField(value = "SECURITY_DEVICE_ASSET_SCORE")
    private Double securityDeviceAssetScore;

    /**
     * 网络资产评分
     */
    @TableField(value = "NETWORK_DEVICE_ASSET_SCORE")
    private Double networkDeviceAssetScore;

    /**
     * IT资产评分
     */
    @TableField(value = "IT_DEVICE_ASSET_SCORE")
    private Double itDeviceAssetScore;

    /**
     * 逻辑资产评分
     */
    @TableField(value = "LOGICAL_ASSET_SCORE")
    private Double logicalAssetScore;

    /**
     * 资产总评分
     */
    @TableField(value = "TOTAL_SCORE")
    private Double totalScore;



}
