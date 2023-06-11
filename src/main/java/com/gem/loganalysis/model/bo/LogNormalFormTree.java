package com.gem.loganalysis.model.bo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gem.loganalysis.model.vo.SopLogNormalFormNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 范式树
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 22:43
 */
@NoArgsConstructor
public class LogNormalFormTree {

    @Getter
    private SopLogNormalFormNode root;

    public LogNormalFormTree(List<SopLogNormalFormNode> list) {
        // 创建根节点
        this.root = new SopLogNormalFormNode();
        this.root.setFieldId("0");
        this.root.setFieldName("root");
        this.root.setFieldName("根节点");
        this.root.setLevel(0);
        this.root.setPid("-1");
        this.root.setChildren(new ArrayList<>());

        // 构建节点信息
        listToTree(list);
    }

    protected LogNormalFormTree newInstance() {
        LogNormalFormTree newTree = new LogNormalFormTree();
        BeanUtil.copyProperties(this, newTree);
        return newTree;
    }

    private void listToTree(List<SopLogNormalFormNode> list) {
        HashMap<String, SopLogNormalFormNode> cache = new HashMap<>(list.size());
        // List转Tree
        for (SopLogNormalFormNode node : list) {
            String code = node.getFieldId();
            String pCode = node.getPid();
            if (StrUtil.isNotEmpty(code)) {
                if ("0".equals(pCode)) {
                    this.root.getChildren().add(node);
                } else {
                    SopLogNormalFormNode pNode = cache.get(pCode);
                    if (pNode != null) {
                        if (pNode.getChildren() == null) {
                            pNode.setChildren(new ArrayList<>());
                        }
                        pNode.getChildren().add(node);
                    }
                }
                cache.put(code, node);
            }
        }
    }

    protected void formulation(HashMap<String, Object> messageInfoMap) {
        formulation(this.root, messageInfoMap);
    }

    /**
     * 将原始日志结构体根据映射规则范式化
     *
     * @param node           节点
     * @param messageInfoMap 原始日志Map对象
     */
    private void formulation(SopLogNormalFormNode node, HashMap<String, Object> messageInfoMap) {
        List<SopLogNormalFormNode> children = node.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            for (SopLogNormalFormNode child : children) {
                formulation(child, messageInfoMap);
            }
        }
        // 填充范式对象Value
        String sourceFieldName = node.getSourceFieldName();
        String fieldValue = getFieldValueFromMap(messageInfoMap, sourceFieldName);
        node.setFieldValue(fieldValue);
    }

    /**
     * 从JSON化日志Map对象中获取目标属性的值
     *
     * @param messageInfoMap map
     * @param fieldName      属性名
     * @return 属性值
     */
    private String getFieldValueFromMap(HashMap<String, Object> messageInfoMap, String fieldName) {
        if (CollUtil.isNotEmpty(messageInfoMap) && StrUtil.isNotEmpty(fieldName)) {
            int splitChar = fieldName.indexOf(".");
            if (splitChar != 0) {
                String preField = fieldName.substring(0, splitChar);
                return getFieldValueFromMap((HashMap<String, Object>) messageInfoMap.get(preField), fieldName.substring(splitChar + 1));
            } else {
                return (String) messageInfoMap.get(fieldName);
            }
        }
        return null;
    }

    /**
     * 根据属性名称查询Value
     *
     * @param fieldName 属性名(多层级通过.分割)
     * @return fieldValue
     */
    protected String getFieldValue(String fieldName) {
        return getFieldValue(root.getChildren(), fieldName);
    }

    /**
     * 递归查找
     *
     * @param nodeList
     * @param fieldName
     * @return
     */
    private String getFieldValue(List<SopLogNormalFormNode> nodeList, String fieldName) {
        if (CollUtil.isNotEmpty(nodeList)) {
            int splitChar = fieldName.indexOf(".");
            for (SopLogNormalFormNode node : nodeList) {
                if (splitChar != 0) {
                    String preField = fieldName.substring(0, splitChar);
                    if (preField.equals(node.getFieldName())) {
                        return getFieldValue(node.getChildren(), fieldName.substring(splitChar + 1));
                    }
                } else {
                    if (fieldName.equals(node.getFieldName())) {
                        return node.getFieldValue();
                    }
                }
            }
        }
        return null;
    }

    public String toJsonStr() {
        return null;
    }

}
