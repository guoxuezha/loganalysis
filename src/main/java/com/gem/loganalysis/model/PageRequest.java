package com.gem.loganalysis.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wangwenhao
 * @create 2021/12/15
 */
@Data
@Builder
@NoArgsConstructor
public class PageRequest<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "页码，从 1 开始", required = true,example = "1")
    @NotBlank(message = "当前页不能为空")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页条数", required = true, example = "10")
    @NotBlank(message = "每页数量不能为空")
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 分页请求对象参数
     */
    @Valid
    private T data;

    private String userId;

    public PageRequest(Integer pageNum, Integer pageSize, String orderBy, T data, String userId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.data = data;
        this.userId = userId;
        if (pageNum == 0) {
            this.pageNum = 1;
        }
        if (pageSize == 0) {
            this.pageSize = 10;
        }
    }

}
