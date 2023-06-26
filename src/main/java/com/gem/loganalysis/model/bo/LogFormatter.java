package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.service.ILogAnalysisRuleRelaService;
import com.gem.loganalysis.util.SpringContextUtil;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志范式化
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 21:42
 */
public class LogFormatter {

    private Integer ruleType;

    private String jarName;

    private String version;

    private String methodName;

    /**
     * 分段字符序列
     */
    private String itemSplitSequence;

    /**
     * 分值字符序列
     */
    private String kvSplitSequence;

    /**
     * 日志范式映射关系
     */
    @Getter
    private LogNormalFormTree tree;

    protected LogFormatter(String ruleRelaId) {
        init(ruleRelaId);
    }

    private void init(String ruleRelaId) {
        // 初始化日志解析分词规则
        String querySql = "SELECT A.RULE_RELA_ID, B.RULE_TYPE, B.METHOD_NAME, B.ITEM_SPLIT, B.KV_SPLIT, C.JAR_NAME, C.VERSION "
                + "FROM SOP_LOG_ANALYSIS_RULE_RELA A "
                + "LEFT JOIN sop_log_analysis_rule B ON A.ANALYSIS_RULE_ID = B.ANALYSIS_RULE_ID "
                + "LEFT JOIN sop_log_analysis_customization_rule C ON B.METHOD_NAME = C.METHOD_NAME "
                + "WHERE A.RULE_RELA_ID = '" + ruleRelaId + "' LIMIT 1";
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql);
        if (CollUtil.isNotEmpty(dataSet)) {
            HashMap<String, String> ruleMap = dataSet.get(0);
            if (ruleMap.get("RULE_TYPE") != null) {
                this.ruleType = Integer.valueOf(ruleMap.get("RULE_TYPE"));
                this.itemSplitSequence = ruleMap.get("ITEM_SPLIT");
                this.kvSplitSequence = ruleMap.get("KV_SPLIT");
                this.jarName = ruleMap.get("JAR_NAME");
                this.version = ruleMap.get("VERSION");
                this.methodName = ruleMap.get("METHOD_NAME");
            }
        }

        // 初始化日志解析规则范式映射关系
        freshTree(ruleRelaId);
    }

    protected void freshTree(String ruleRelaId) {
        ILogAnalysisRuleRelaService bean = SpringContextUtil.getBean(ILogAnalysisRuleRelaService.class);
        tree = bean.showLogNormalForm(ruleRelaId);
    }

    /**
     * 原始日志范式化
     *
     * @param message 原始日志字符串
     * @return 范式化后的日志对象
     */
    public LogNormalFormTree format(String message) {
        // 先根据分词规则将日志字符串JSON序列化
        HashMap<String, Object> messageInfoMap = getMessageInfoMap(message);

        // 再根据属性映射关系将原始日志范式化
        LogNormalFormTree newTree = tree.newInstance();
        newTree.formulation(messageInfoMap);
        return newTree;
    }

    public SimpleTreeNode formatSourceLog(String message) {
        HashMap<String, Object> infoMap = getMessageInfoMap(message);
        SimpleTreeNode root = new SimpleTreeNode();
        root.setId("root");
        root.setLabel("root");
        root.setChildren(loadSourceLogInNode(infoMap));
        return root;
    }

    private List<SimpleTreeNode> loadSourceLogInNode(HashMap<String, Object> infoMap) {
        List<SimpleTreeNode> nodes = new ArrayList<>();
        for (Map.Entry<String, Object> entry : infoMap.entrySet()) {
            SimpleTreeNode node = new SimpleTreeNode();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<>());
                }
                HashMap<String, Object> childMap = (HashMap<String, Object>) value;
                node.setChildren(loadSourceLogInNode(childMap));
            }
            node.setId(key);
            node.setLabel(key);
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * JSON格式化message
     *
     * @param msg 日志消息体
     * @return 格式化后的日志消息体
     */
    private HashMap<String, Object> getMessageInfoMap(String msg) {
        HashMap<String, Object> map = new HashMap<>();
        if (this.ruleType == null) {
            try {
                map = new ObjectMapper().readValue(msg, HashMap.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else if (this.ruleType == 1) {
            if (StrUtil.isEmpty(itemSplitSequence) && StrUtil.isEmpty(kvSplitSequence)) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.readValue(msg, HashMap.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            for (String kv : msg.trim().split(itemSplitSequence)) {
                String[] split = kv.split(kvSplitSequence);
                if (split.length == 2) {
                    map.put(split[0], split[1]);
                } else if (split.length == 1) {
                    map.put(split[0], null);
                } else {
                    map.put(split[0], kv.substring(split[0].length()));
                }
            }
        } else {
            if (StrUtil.isNotEmpty(jarName) && StrUtil.isNotEmpty(version) && StrUtil.isNotEmpty(methodName)) {
                Object result = SpringContextUtil.invokeClassMethod(jarName + "-CustomizationAnalysis" + version, methodName, msg);
                if (result != null) {
                    map = (HashMap<String, Object>) result;
                }
            }
        }
        return map;
    }

    @Data
    public static class SimpleTreeNode {

        private String id;

        private String label;

        private List<SimpleTreeNode> children;

    }

}
