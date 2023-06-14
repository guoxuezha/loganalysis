package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author czw
 * @version 1.0
 * @date 2023/6/12 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("首页总览-事件 VO Response ")
public class AssetEventHomeOverviewVO {

    @ApiModelProperty(value = "资产类别")
    private String assetType;

    @ApiModelProperty(value = "今日全部事件")
    private Integer totalEventCount;

    @ApiModelProperty(value = "今日待处理事件")
    private Integer pendingEventCount;

}
