package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/17 15:24
 */
@Data
public class LogFileQueryDTO {

    @ApiModelProperty("桶名")
    private String fileName;

}
