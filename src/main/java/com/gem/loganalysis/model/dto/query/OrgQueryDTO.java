package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织部门查询请求类
 *
 * @author czw
 * @since 2023-02-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统模块 - 组织部门查询 DTO ")
public class OrgQueryDTO {

    @ApiModelProperty(value = "组织部门名称(惯用名)")
    private String orgName;

}
