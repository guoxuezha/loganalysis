package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 15:47
 */
@Data
public class TaskDTO {

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty(value = "工作ID", required = true)
    private String jobId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务描述")
    private String taskDesc;

    @ApiModelProperty(value = "调用模式(空/STATIC/URL/SERVLET/WEBSERVICE)", required = true)
    private String callMode;

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("调用路径")
    private String url;

    @ApiModelProperty("ServiceContent")
    private String content;

}
