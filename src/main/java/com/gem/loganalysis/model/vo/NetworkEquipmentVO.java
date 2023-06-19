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
public class NetworkEquipmentVO {

    @ApiModelProperty(value = "设备ID(不展示)")
    private String assetId;

    @ApiModelProperty(value = "设备名称")
    private String assetName;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "品牌")
    private String assetBrand;

    @ApiModelProperty(value = "型号")
    private String assetModel;

    //TODO  放入 运行状态 正常 异常 告警等
    @ApiModelProperty(value = "运行状态")
    private String runningState = "正常";

    @ApiModelProperty(value = "所属部门")
    private String orgName;

    @ApiModelProperty(value = "设备类型")
    private String typeName;

    @ApiModelProperty(value = "当日事件次数")
    private Integer todayEventCount;
}
