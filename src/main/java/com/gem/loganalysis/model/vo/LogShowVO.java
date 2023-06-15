package com.gem.loganalysis.model.vo;

import com.gem.loganalysis.model.bo.LogEventParamConfig;
import com.gem.loganalysis.model.bo.LogNormalFormTree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

/**
 * 日志内容展示VO
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/13 10:40
 */
@Data
public class LogShowVO {

    /**
     * 源端IP
     */
    @ApiModelProperty("源端IP")
    private String sourceIp;

    /**
     * 目标IP
     */
    @ApiModelProperty("目标IP")
    private String targetIp;

    /**
     * 风险级别
     */
    @ApiModelProperty("风险级别")
    private String riskLevel;

    /**
     * 事件类型
     */
    @ApiModelProperty("事件类型")
    private String eventType;

    /**
     * 归并日志详情
     */
    @ApiModelProperty("归并日志详情")
    private HashMap<String, Object> detail;
//    private String detail;

    public LogShowVO(String message) {
        LogNormalFormTree tree = new LogNormalFormTree(message);
        LogEventParamConfig eventParamConfig = LogEventParamConfig.getInstance();
        sourceIp = tree.getFieldValue(eventParamConfig.getSourceIpItem());
        targetIp = tree.getFieldValue(eventParamConfig.getTargetIpItem());
        riskLevel = tree.getFieldValue(eventParamConfig.getRiskLevelItem());
        eventType = tree.getFieldValue(eventParamConfig.getEventTypeItem());
        detail = tree.toJsonObject(true);
    }

}
