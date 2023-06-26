package com.gem.loganalysis.model.vo;

import com.gem.loganalysis.model.PageResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/26 11:30
 */
@Data
public class SourceLogVO {

    @ApiModelProperty("表头")
    private Object header;

    @ApiModelProperty("记录")
    private PageResponse<Object> record;

}
