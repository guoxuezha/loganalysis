package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * IP设备扫描结果
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_physical_asset_temp")
public class PhysicalAssetTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一编码
     */
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_ID)
    private String recordId;

    /**
     * 资产所属组织（部门）
     */
    @TableField("ASSET_ORG")
    private String assetOrg;

    /**
     * IP地址
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * 资产状态（0无1有）
     */
    @TableField("ASSET_STATUS")
    private String assetStatus;

    /**
     * 资产扫描批次时间
     */
    @TableField("SCAN_TIME")
    private String scanTime;

    /**
     * 类型 0自动 1手动
     */
    @TableField("TYPE")
    private Integer type;


}
