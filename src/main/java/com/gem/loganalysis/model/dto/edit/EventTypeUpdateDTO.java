package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/25 22:37
 */
@Data
public class EventTypeUpdateDTO {

    @ApiModelProperty(value = "事件类型(新)", required = true)
    private String eventType;

    @ApiModelProperty(value = "事件等级(新)", required = true)
    private String eventClass;

    @ApiModelProperty(value = "封堵规则编码(新)", required = true)
    private String blockRuleId;

    @ApiModelProperty(value = "事件类型(旧)", required = true)
    private String eventTypeOld;

    @ApiModelProperty(value = "事件等级(旧)", required = true)
    private String eventClassOld;

    @ApiModelProperty(value = "封堵规则编码(旧)", required = true)
    private String blockRuleIdOld;

}
