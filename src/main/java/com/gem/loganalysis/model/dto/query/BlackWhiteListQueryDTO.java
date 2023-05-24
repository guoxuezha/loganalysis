package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统配置 - 黑白名单查询 DTO ")
@Data
public class BlackWhiteListQueryDTO {

    @ApiModelProperty(value = "组织机构唯一编码")
    private String orgId;

    @ApiModelProperty(value = "关联资产唯一编码")
    private String assetId;

    @ApiModelProperty(value = "资产IP地址")
    private String assetIp;

    @ApiModelProperty(value = "列入黑名单的IP")
    private String ipAddress;

    @ApiModelProperty(value = "有效时间时间范围查询,起始时间")
    private String beginValidTime;

    @ApiModelProperty(value = "有效时间时间范围查询,结束时间")
    private String endValidTime;

}
