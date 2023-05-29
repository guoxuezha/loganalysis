package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * <p>
 * 物理资产发现规则
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_physical_asset_discovery_rule")
public class PhysicalAssetDiscoveryRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一编码
     */
    @TableId(value = "PHYSICAL_ASSET_DISCOVERY_RULE_ID", type = IdType.ASSIGN_ID)
    private String physicalAssetDiscoveryId;

    /**
     * community字符串
     */
    @TableField("COMMUNITY")
    private String community;

    /**
     * JSON格式的多组OID
     */
    @TableField("OID")
    private String oid;

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
