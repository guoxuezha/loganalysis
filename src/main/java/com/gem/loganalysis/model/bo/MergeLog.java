package com.gem.loganalysis.model.bo;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: Rsyslog模板化输出格式
 * Date: 2023/4/21 9:07
 *
 * @author GuoChao
 **/
@Data
public class MergeLog {

    /**
     * 归并日志ID
     */
    @ApiModelProperty("归并日志ID")
    private String logId;

    /**
     * 业务联合主键
     */
    @ApiModelProperty("业务联合主键")
    private String unionKey;

    /**
     * 日志类型
     */
    @ApiModelProperty("日志类型")
    private int logType;
    /**
     * 时间戳
     */
    @ApiModelProperty("时间戳")
    private String timestamp;
    /**
     * 消息发生IP
     */
    @ApiModelProperty("消息发生IP")
    private String host;
    /**
     * 日志优先级
     */
    @ApiModelProperty("日志优先级")
    private String severity;
    /**
     * 日志发生的子系统
     */
    @ApiModelProperty("日志发生的子系统")
    private String facility;
    /**
     * 消息体
     */
    @ApiModelProperty("消息体")
    private String message;
    /**
     * 消息标签
     */
    @ApiModelProperty("消息标签")
    private String tag;
    /**
     * 归并周期内日志出现次数
     */
    @ApiModelProperty("归并周期内日志出现次数")
    private AtomicInteger mergeCount;

    public void generateLogId() {
        logId = IdUtil.simpleUUID();
        mergeCount = new AtomicInteger(0);
    }

    public LogAnalysisRuleBo.SimpleQueueMessageInfo toSimpleQueueMessageInfo() {
        return new LogAnalysisRuleBo.SimpleQueueMessageInfo(unionKey, timestamp);
    }

}
