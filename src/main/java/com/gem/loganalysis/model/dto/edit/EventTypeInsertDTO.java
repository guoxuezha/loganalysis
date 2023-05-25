package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/25 22:29
 */
@Data
public class EventTypeInsertDTO {

    @ApiModelProperty("事件类型")
    private String eventType;

    @ApiModelProperty("事件等级")
    private String eventClass;

    @ApiModelProperty("封堵规则编码")
    private String blockRuleId;

}
