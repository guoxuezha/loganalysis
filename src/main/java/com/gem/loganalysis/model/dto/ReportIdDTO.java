package com.gem.loganalysis.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class ReportIdDTO {

    @ApiModelProperty(value = "reportId")
    private String reportId;

}
