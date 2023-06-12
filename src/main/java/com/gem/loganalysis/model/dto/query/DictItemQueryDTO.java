package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字典类型查询请求类
 *
 * @author czw
 * @since 2023-02-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)//链式
public class DictItemQueryDTO{

    @ApiModelProperty(value = "字典编码")
    private String code;
/*
    @ApiModelProperty(value = "字典类型ID")
    private Integer typeId;*/

/*    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;*/
/*
    @ApiModelProperty(value = "上级ID")
    private Integer parentId;*/

}
