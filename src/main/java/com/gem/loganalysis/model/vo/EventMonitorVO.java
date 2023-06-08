package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 事件监控视图类
 *
 * @author czw
 * @version 1.0
 * @date 2023/5/24 21:49
 */
@Data
@ApiModel("事件监控VO Response ")
public class EventMonitorVO {

    @ApiModelProperty(value = "网络设备")
    private List<NetworkEquipmentVO> networkEquipmentList;

    @ApiModelProperty(value = "安全设备")
    private List<NetworkEquipmentVO> safetyDEquipmentList;

    @ApiModelProperty(value = "IT设备")
    private List<ITEquipmentVO> itEquipmentList;

    @ApiModelProperty(value = "终端设备")
    private List<TerminalEquipmentVO> terminalEquipmentList;

}
