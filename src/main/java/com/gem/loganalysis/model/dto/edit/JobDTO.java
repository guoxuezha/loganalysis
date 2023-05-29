package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 15:26
 */
@Data
public class JobDTO {

    @ApiModelProperty("ID")
    private String jobId;

    @ApiModelProperty("工作名称")
    private String jobName;

    @ApiModelProperty("是否启动执行(0否 1是)")
    private Integer executeOnStart;

    @ApiModelProperty("首次执行时间")
    private String firstFiredTime;

    @ApiModelProperty("执行周期定义")
    private String cycleDefine;

}
