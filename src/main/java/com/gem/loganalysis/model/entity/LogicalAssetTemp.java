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
 * 逻辑资产扫描结果
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_logical_asset_temp")
public class LogicalAssetTemp implements Serializable {

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
     * 可用端口
     */
    @TableField("ENABLE_PORT")
    private Integer enablePort;

    /**
     * 推测的资产类型
     */
    @TableField("ASSET_TYPE")
    private String assetType;

    /**
     * 扫描获得的反馈信息
     */
    @TableField("ASSET_INFO")
    private String assetInfo;

    /**
     * 资产扫描批次时间
     */
    @TableField("SCAN_TIME")
    private String scanTime;

    /**
     * 扫描类型 0自动 1手动
     */
    @TableField("TYPE")
    private Integer type;


}
