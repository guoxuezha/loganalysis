package com.gem.loganalysis.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class IpSectionDTO {

    @ApiModelProperty(value = "资产所属组织(部门)",required = true)
    @NotNull(message = "请传入资产所属组织ID")
    private String orgId;

}
