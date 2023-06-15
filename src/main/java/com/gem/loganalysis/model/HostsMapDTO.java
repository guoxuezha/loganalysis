package com.gem.loganalysis.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class HostsMapDTO {

    @ApiModelProperty(value = "hosts")
    private Map<String, String> hosts;

}
