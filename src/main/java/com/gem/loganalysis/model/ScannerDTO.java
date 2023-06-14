package com.gem.loganalysis.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class ScannerDTO {

    @ApiModelProperty(value = "扫描类型，SIMPLE,ALL")
    private String scannerType;

}
