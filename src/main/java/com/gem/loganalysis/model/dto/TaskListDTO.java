package com.gem.loganalysis.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author czw
 * @version 1.0
 * @date 2023/5/4 11:53
 */
@Data
public class TaskListDTO {

    @ApiModelProperty(value = "taskList")
    private List<String> taskList;

}
