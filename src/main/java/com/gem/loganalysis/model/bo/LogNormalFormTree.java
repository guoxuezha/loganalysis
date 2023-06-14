package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gem.loganalysis.model.vo.SopLogNormalFormNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 范式树
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 22:43
 */
@NoArgsConstructor
public class LogNormalFormTree implements Serializable {

    @Getter
    private SopLogNormalFormNode root;

    private LogFormatterServer logFormatterServer;

    public LogNormalFormTree(List<SopLogNormalFormNode> list) {
        logFormatterServer = LogFormatterServer.getInstance();
        // 创建根节点
        this.root = new SopLogNormalFormNode();
        this.root.setId("0");
        this.root.setLabel("logRoot");
        this.root.setFieldDesc("根节点");
        this.root.setLevel(0);
        this.root.setPid("-1");
        this.root.setChildren(new ArrayList<>());

        // 构建节点信息
        listToTree(list);
    }

    public LogNormalFormTree(String mergeLogMessage) {
        logFormatterServer = LogFormatterServer.getInstance();
        root = loadNodeInfo(mergeLogMessage).get(0);
    }

    private List<SopLogNormalFormNode> loadNodeInfo(String mergeLogMessage) {
        List<SopLogNormalFormNode> nodes = new ArrayList<>();
        JSONObject jsonObject = JSONUtil.parseObj(mergeLogMessage);
        for (Map.Entry<String, Object> entry : jsonObject) {
            SopLogNormalFormNode node = new SopLogNormalFormNode();
            String itemName = entry.getKey();
            String itemDesc = this.logFormatterServer.itemKVMap.get(itemName);
            String valueStr = entry.getValue().toString();
            node.setLabel(itemName);
            node.setFieldDesc(itemDesc);
            if (valueStr.contains("{")) {
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<>());
                }
                node.setChildren(loadNodeInfo(valueStr));
            } else {
                node.setFieldValue(valueStr);
            }
            nodes.add(node);
        }
        return nodes;
    }

    protected LogNormalFormTree newInstance() {
        return ObjectUtil.clone(this);
    }

    private void listToTree(List<SopLogNormalFormNode> list) {
        HashMap<String, SopLogNormalFormNode> cache = new HashMap<>(list.size());
        // List转Tree
        for (SopLogNormalFormNode node : list) {
            String code = node.getId();
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
            if (splitChar > 0) {
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
    public String getFieldValue(String fieldName) {
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
                if (splitChar > 0) {
                    String preField = fieldName.substring(0, splitChar);
                    if (preField.equals(node.getLabel())) {
                        return getFieldValue(node.getChildren(), fieldName.substring(splitChar + 1));
                    }
                } else {
                    if (fieldName.equals(node.getLabel())) {
                        return node.getFieldValue();
                    }
                }
            }
        }
        return null;
    }

    public String toJsonStr(boolean turnKeyToDesc) {
        HashMap<String, Object> result;
        if (turnKeyToDesc) {
            result = getShowNodeKV(root);
        } else {
            result = getNodeKV(root);
        }
        return JSONUtil.toJsonStr(result);
    }

    private HashMap<String, Object> getNodeKV(SopLogNormalFormNode node) {
        HashMap<String, Object> result = new HashMap<>();
        if (CollUtil.isEmpty(node.getChildren())) {
            result.put(node.getLabel(), node.getFieldValue());
        } else {
            HashMap<String, Object> childInfoMap = new HashMap<>(node.getChildren().size());
            for (SopLogNormalFormNode child : node.getChildren()) {
                childInfoMap.putAll(getNodeKV(child));
            }
            result.put(node.getLabel(), childInfoMap);
        }
        return result;
    }

    private HashMap<String, Object> getShowNodeKV(SopLogNormalFormNode node) {
        HashMap<String, Object> result = new HashMap<>();
        String key = node.getFieldDesc() != null ? node.getFieldDesc() : node.getLabel();
        if (CollUtil.isEmpty(node.getChildren())) {
            result.put(key, node.getFieldValue());
        } else {
            HashMap<String, Object> childInfoMap = new HashMap<>(node.getChildren().size());
            for (SopLogNormalFormNode child : node.getChildren()) {
                childInfoMap.putAll(getShowNodeKV(child));
            }
            result.put(key, childInfoMap);
        }
        return result;
    }

}
