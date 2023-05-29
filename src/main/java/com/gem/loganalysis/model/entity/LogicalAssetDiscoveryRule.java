package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * <p>
 * 逻辑资产发现规则
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_logical_asset_discovery_rule")
public class LogicalAssetDiscoveryRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一编码
     */
    @TableId(value = "LOGICAL_ASSET_DISCOVERY_RULE_ID", type = IdType.ASSIGN_ID)
    private String logicalAssetDiscoveryId;

    /**
     * 端口号
     */
    @TableField("PORT")
    private Integer port;

    /**
     * 网络协议
     */
    @TableField("NETWORK_PROTOCOL")
    private String networkProtocol;

    /**
     * 资产类型
     */
    @TableField("ASSET_TYPE")
    private String assetType;

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
