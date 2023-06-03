package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 21:58
 */
@Data
public class BlockRuleDTO {

    @ApiModelProperty("封堵规则ID")
    private String blockRuleId;

    @ApiModelProperty("规则描述")
    private String blockRuleDesc;

    @ApiModelProperty("封堵类型（0临时/1永久）")
    private Integer blockType;

    @ApiModelProperty("封堵时长（分钟）")
    private Integer blockDuration;

    @ApiModelProperty("启用白名单（0否1是）")
    private Integer whiteListEnable;

    @ApiModelProperty("启用黑名单（0否1是）")
    private Integer blackListEnable;

    @ApiModelProperty("执行操作的资产(防火墙)ID(若有多个则使用,分割)")
    private String operationAssetId;

}
