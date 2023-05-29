package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 15:24
 */
@Data
public class JobQueryDTO {

    @ApiModelProperty("工作名")
    private String jobName;

    @ApiModelProperty("当前调度状态")
    private Integer currentStatus;

    @ApiModelProperty("第一次执行开始时间")
    private String firstFiredStartTime;

    @ApiModelProperty("第一次执行结束时间")
    private String firstFiredEndTime;

}
