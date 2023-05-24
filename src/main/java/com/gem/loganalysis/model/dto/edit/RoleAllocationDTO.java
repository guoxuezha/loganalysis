package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色分配
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/24 16:23
 */
@Data
public class RoleAllocationDTO {

    @ApiModelProperty(value = "用户ID", required = true)
    private String userId;

    @ApiModelProperty(value = "角色ID列表", required = true)
    private List<String> roleIdList;

}
