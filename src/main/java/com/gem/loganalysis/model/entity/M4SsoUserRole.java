package com.gem.loganalysis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统用户角色表
 * </p>
 *
 * @author GuoChao
 * @since 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m4_sso_user_role")
@NoArgsConstructor
public class M4SsoUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一编码
     */
    @TableId("USER_ID")
    @ApiModelProperty("用户唯一编码")
    private String userId;

    /**
     * 业务包编码
     */
    @TableField("PACKAGE_ID")
    @ApiModelProperty("业务包编码")
    private String packageId;

    /**
     * 角色编码
     */
    @TableField("ROLE_ID")
    @ApiModelProperty("角色编码")
    private String roleId;

    /**
     * 系统用户角色可用状态
     */
    @TableField("ENABLE_STATUS")
    @ApiModelProperty("系统用户角色可用状态")
    private Integer enableStatus;

    public M4SsoUserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
        this.packageId = "01";
        this.enableStatus = 1;
    }


}
