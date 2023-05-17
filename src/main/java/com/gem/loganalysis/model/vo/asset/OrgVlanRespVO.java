package com.gem.loganalysis.model.vo.asset;

import com.gem.loganalysis.model.dto.asset.VlanDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/17 9:11
 */
@ApiModel("资产模块 - VLAN设定 Response ")
@Data
public class OrgVlanRespVO {

    @ApiModelProperty(value = "组织机构唯一编码")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称（惯用名）")
    private String orgName;

    @ApiModelProperty(value = "JSON格式的VLAN")
    private String vlan;

    @ApiModelProperty(value = "IP区间")
    private List<VlanDTO> vlanList;


}
