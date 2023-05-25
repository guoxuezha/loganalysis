package com.gem.loganalysis.model.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gem.loganalysis.model.dto.edit.RoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 业务包中的角色
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m4_sso_package_role")
@NoArgsConstructor
public class M4SsoPackageRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务包编码
     */
    @TableField("PACKAGE_ID")
    private String packageId;

    /**
     * 角色编码
     */
    @TableId("ROLE_ID")
    private String roleId;

    /**
     * 角色名称
     */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 角色说明
     */
    @TableField("ROLE_DESC")
    private String roleDesc;

    /**
     * 角色可用状态
     */
    @TableField("ENABLE_STATUS")
    private Integer enableStatus;

    public M4SsoPackageRole(RoleDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.packageId = "01";
        this.enableStatus = 1;
    }

}
