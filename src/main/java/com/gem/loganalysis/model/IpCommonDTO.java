package com.gem.loganalysis.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class IpCommonDTO {

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "comment")
    private String comment;

}
