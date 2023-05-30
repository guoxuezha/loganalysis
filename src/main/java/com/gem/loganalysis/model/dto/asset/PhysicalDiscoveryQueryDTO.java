package com.gem.loganalysis.model.dto.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资产模块 - 物理资产扫描搜索 DTO ")
@Data
public class PhysicalDiscoveryQueryDTO {


    @ApiModelProperty(value = "community字符串")
    private String community;

    @ApiModelProperty(value = "OID")
    private String oid;



}
