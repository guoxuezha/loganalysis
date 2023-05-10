package com.gem.loganalysis.model.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;

/**
 * 安全管理资产 DO
 *
 * @author czw
 */
@TableName("SOP_ASSET")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    /**
     * 资产唯一编码
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String assetId;
    /**
     * 资产名称
     */
    private String assetName;
    /**
     * 资产分类，物理资产/逻辑资产
     */
    private String assetClass;
    /**
     * 资产类型，服务器/路由器/交换机/防火墙或者数据库/中间件/网络应用/域名等
     */
    private String assetType;
    /**
     * 资产描述
     */
    private String assetDesc;
    /**
     * 品牌
     */
    private String assetBrand;
    /**
     * 型号
     */
    private String assetModel;
    /**
     * 规格
     */
    private String assetSpec;
    /**
     * 操作系统版本
     */
    private String osVersion;
    /**
     * 固件版本
     */
    private String firmwareVersion;
    /**
     * 软件版本
     */
    private String softwareVersion;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 服务端口
     */
    private Integer servicePort;
    /**
     * 网管端口
     */
    private Integer nmPort;
    /**
     * 网管协议
     */
    private String nmProcotol;
    /**
     * 网管账号
     */
    private String nmAccount;
    /**
     * 网管密码（加密存储）
     */
    private String nmPassword;
    /**
     * 资产所属组织（部门）
     */
    private String assetOrg;
    /**
     * 资产管理人
     */
    private String assetManager;
    /**
     * 资产分组
     */
    private String assetGroupId;
    /**
     * 资产标签
     */
    private String assetTag;
    /**
     * 资产状态（0在役/1退役/2在线/3离线）
     */
    private String assetStatus;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateTime;
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;
    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;
    /**
     * 删除标记
     */
    @TableLogic
    private Integer deleteState;

}
