package com.gem.loganalysis.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

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
public class SopLogNormalFormNode {

    /**
     * 日志项ID
     */
    private String fieldId;

    /**
     * 日志项名称(Key)
     */
    private String fieldName;

    /**
     * 日志项描述(中文)
     */
    private String fieldDesc;

    /**
     * 父节点ID
     */
    private String pid;

    /**
     * 所属层级
     */
    private Integer level;

    /**
     * 原始日志属性名称
     */
    private String sourceFieldName;

    /**
     * 属性值
     */
    private String fieldValue;

    /**
     * 子节点
     */
    private List<SopLogNormalFormNode> children;

}
