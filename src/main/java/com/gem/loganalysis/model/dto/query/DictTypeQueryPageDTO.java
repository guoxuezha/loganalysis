package com.gem.loganalysis.model.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 字典数据查询请求类
 *
 * @author czw
 * @since 2023-02-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictTypeQueryPageDTO{

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "当前页", name = "curPage", required = true, example = "1")
    @NotNull(message = "当前页不能为空")
    private Integer curPage;

    @ApiModelProperty(value = "每页数量", name = "pageSize", required = true, example = "10")
    @NotNull(message = "每页数量不能为空")
    private Integer pageSize;

}
