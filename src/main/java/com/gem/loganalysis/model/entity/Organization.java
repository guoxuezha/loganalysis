package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 组织机构
 *
 * @author GuoChao
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    @TableId("ORG_ID")
    private String orgId;

    /**
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 组织描述
     */
    @TableField("ORG_DESC")
    private String orgDesc;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标记
     */
    @TableField("DELETE_STATE")
    private Integer deleteState;


}
