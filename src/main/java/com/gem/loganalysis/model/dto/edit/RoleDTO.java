package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 16:13
 */
@Data
public class RoleDTO {

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private String roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色说明
     */
    @ApiModelProperty("角色说明")
    private String roleDesc;

}
