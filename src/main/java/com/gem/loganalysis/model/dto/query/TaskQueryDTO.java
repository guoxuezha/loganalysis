package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 15:47
 */
@Data
public class TaskQueryDTO {

    @ApiModelProperty("JobId")
    private String jobId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("调用模式(空/STATIC/URL/SERVLET/WEBSERVICE)")
    private String callMode;

}
