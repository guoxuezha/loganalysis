package com.gem.loganalysis.model.entity;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;

/**
 * 资产分组 DO
 *
 * @author czw
 */
@TableName("SOP_ASSET_GROUP")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetGroup{

    private static final long serialVersionUID = 1L;

    /**
     * 资产所属组织（部门）
     */
    private String assetOrg;
    /**
     * 资产分组编码
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String groupId;
    /**
     * 资产分组名称
     */
    private String groupName;
    /**
     * 资产分组说明
     */
    private String groupDesc;
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
