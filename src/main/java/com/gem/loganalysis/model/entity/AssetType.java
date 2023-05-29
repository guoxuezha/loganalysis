package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 资产类型
 *
 * @author czw
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sop_asset_type")
public class AssetType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资产类型ID
     */
    @TableId(value = "TYPE_ID",type = IdType.AUTO)
    private Integer typeId;

    /**
     * 资产大类型
     */
    @TableField("ASSET_TYPE")
    private String assetType;

    /**
     * 类型名称
     */
    @TableField("TYPE_NAME")
    private String typeName;

    /**
     * 厂家列表
     */
    @TableField("ASSET_BRAND")
    private String assetBrand;

    /**
     * 型号列表
     */
    @TableField("ASSET_MODEL")
    private String assetModel;

    /**
     * 系统
     */
    @TableField("OS_VERSION")
    private String osVersion;

    /**
     * 删除标记
     */
    @TableField("DELETE_STATE")
    @TableLogic
    private Integer deleteState;

}
