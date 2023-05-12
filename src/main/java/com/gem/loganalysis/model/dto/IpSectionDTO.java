package com.gem.loganalysis.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class IpSectionDTO {

    @ApiModelProperty(value = "IP区段")
    private String ipSection;
}
