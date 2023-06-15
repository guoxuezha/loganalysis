package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)//链式
@ApiModel("首页总览-风险资产排行 Response ")
public class RiskAssetRankingVO {

    @ApiModelProperty(value = "风险资产名称")
    private String name;

    @ApiModelProperty(value = "评分")
    private Double score;
}
