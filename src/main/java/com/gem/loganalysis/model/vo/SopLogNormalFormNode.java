package com.gem.loganalysis.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 日志范式属性节点对象
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/8 16:49
 */
@Data
@NoArgsConstructor
public class SopLogNormalFormNode implements Serializable {

    /**
     * 日志项ID
     */
    @ApiModelProperty("日志项ID")
    private String id;

    /**
     * 日志项名称(Key)
     */
    @ApiModelProperty("日志项名称(Key)")
    private String label;

    /**
     * 日志项描述(中文)
     */
    @ApiModelProperty("日志项描述(中文)")
    private String fieldDesc;

    /**
     * 父节点ID
     */
    @ApiModelProperty("父节点ID")
    private String pid;

    /**
     * 所属层级
     */
    @ApiModelProperty("所属层级")
    private Integer level;

    /**
     * 原始日志属性名称
     */
    @ApiModelProperty("原始日志属性名称")
    private String sourceFieldName;

    /**
     * 属性值
     */
    @ApiModelProperty("属性值")
    private String fieldValue;

    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<SopLogNormalFormNode> children;

}
