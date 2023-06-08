package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalEquipmentVO {
    @ApiModelProperty(value = "终端总数")
    private Integer totalNumber;

    @ApiModelProperty(value = "在线总数")
    private Integer onlineNumber;
}
