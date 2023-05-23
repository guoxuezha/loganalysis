package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础下拉列表查询请求类
 *
 * @author huty
 * @since 2022-12-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictSelectQueryDTO {

    @ApiModelProperty(value = "字典类型")
    private String typeCode;

    @ApiModelProperty(value = "父级字典项ID")
    private String parentId;
}
