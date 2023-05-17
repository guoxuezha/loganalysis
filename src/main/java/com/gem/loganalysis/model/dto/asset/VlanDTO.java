package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
@ApiModel("系统配置 - vlan区段")
public class VlanDTO {

    @ApiModelProperty(value = "IP开始区段")
    private String beginIp;

    @ApiModelProperty(value = "IP开始区段")
    private String endIp;
}
