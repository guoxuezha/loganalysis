package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * SNMP参数设置
 * </p>
 *
 * @author GuoChao
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_asset_snmp_config")
public class AssetSnmpConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资产所属组织（部门）
     */
    @TableField("ASSET_ORG")
    private String assetOrg;

    /**
     * IP地址(如非缺省端口则需带端口号，如172.168.1.1:10161)
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * SNMP版本
     */
    @TableField("SNMP_VERSION")
    private String snmpVersion;

    /**
     * SNMP-V1/2c COMMUNITY
     */
    @TableField("SNMP_COMMUNITY")
    private String snmpCommunity;

    /**
     * SNMP-V3 USERNAME
     */
    @TableField("SNMP_USERNAME")
    private String snmpUsername;

    /**
     * SNMP-V3 AUTH_PASSWORD
     */
    @TableField("AUTH_PASSWORD")
    private String authPassword;

    /**
     * SNMP-V3 PRIV_PASSWORD
     */
    @TableField("PRIV_PASSWORD")
    private String privPassword;

    /**
     * SNMP -O
     */
    @TableField("OUTPUT_PARAM")
    private String outputParam;

    /**
     * 资产编码
     */
    @TableField("ASSET_ID")
    private String assetId;


}
