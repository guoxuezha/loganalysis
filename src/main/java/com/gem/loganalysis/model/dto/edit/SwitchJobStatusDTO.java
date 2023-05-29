package com.gem.loganalysis.model.dto.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 16:03
 */
@Data
public class SwitchJobStatusDTO {

    @ApiModelProperty("工作ID")
    private String jobId;

    @ApiModelProperty("工作当前状态 -1: 未就绪 0: 就绪 1: 执行中")
    private Integer currentStatus;

}
