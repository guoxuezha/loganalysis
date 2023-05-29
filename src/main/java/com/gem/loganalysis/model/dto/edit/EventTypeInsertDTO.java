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

    @ApiModelProperty(value = "事件类型",required = true)
    private String eventType;

    @ApiModelProperty(value = "事件等级",required = true)
    private String eventClass;

    @ApiModelProperty(value = "封堵规则编码",required = true)
    private String blockRuleId;

}
