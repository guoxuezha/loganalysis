package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)//链式
public class ITEquipmentVO {

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "设备类型")
    private String typeName;

    @ApiModelProperty(value = "所属部门")
    private String orgName;

    @ApiModelProperty(value = "型号")
    private String assetModel;

    @ApiModelProperty(value = "CPU")
    private String cpu;

    @ApiModelProperty(value = "内存")
    private String memory;

    @ApiModelProperty(value = "磁盘")
    private String disk;

    @ApiModelProperty(value = "网络吞吐量")
    private String networkThroughput;
}
