package com.gem.loganalysis.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class GetDTO {

    @ApiModelProperty(value = "通用ID，传业务对象的ID")
    private String id;

}
