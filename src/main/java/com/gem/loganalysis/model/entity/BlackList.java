package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

/**
 * <p>
 * 黑名单
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_black_list")
public class BlackList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构唯一编码
     */
    @TableId("ORG_ID")
    private String orgId;

    /**
     * 关联资产唯一编码（*表示当前组织机构【含下属】的所有资产）
     */
    @TableField("ASSET_ID")
    private String assetId;

    /**
     * 列入黑名单的IP
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * 有效时间，为空时表示永久有效
     */
    @TableField("VALID_TIME")
    private String validTime;

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
     * 是否删除
     */
    @TableField("DELETE_STATE")
    @TableLogic
    private Integer deleteState;


}
