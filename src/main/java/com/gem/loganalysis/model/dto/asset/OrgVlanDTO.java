package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
@ApiModel("系统配置 - vlan设置 DTO")
public class OrgVlanDTO {

    @ApiModelProperty(value = "组织机构唯一编码")
    private String orgId;

    @ApiModelProperty(value = "IP区间")
    private List<VlanDTO> vlanList;

}
