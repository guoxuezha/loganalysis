package com.gem.loganalysis.model.bo;

import cn.hutool.core.util.StrUtil;
import com.gem.loganalysis.model.entity.SafetyEquipment;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 安全设施业务类对象
 * Date: 2023/5/5 20:54
 *
 * @author GuoChao
 **/
@Data
public class SafetyEquipmentBO {

    protected ConcurrentHashMap<String, RsyslogNormalMessage> cache;

    private SafetyEquipment safetyEquipment;
    /**
     * 日志生产子系统
     */
    private String facility;
    /**
     * 日志级别
     */
    private String severity;
    /**
     * 归并字段列表
     */
    private List<String> mergeItemList;
    /**
     * 归并窗口时长(秒)
     */
    private Long windowTime;
    /**
     * 分段字符序列
     */
    private String itemSplitSequence;
    /**
     * 分值字符序列
     */
    private String kvSplitSequence;
    /**
     * 关注的IP字段名
     */
    private String ipItemName;
    /**
     * 封堵时长
     */
    private Long blockOffSecond;

    public SafetyEquipmentBO() {
        this.cache = new ConcurrentHashMap<>();
    }

    public SafetyEquipmentBO(HashMap<String, String> ruleMap) {
        this.safetyEquipment = new SafetyEquipment();
        safetyEquipment.setEquipId(Integer.valueOf(ruleMap.get("EQUIP_ID")));
        safetyEquipment.setIp(ruleMap.get("IP"));
        safetyEquipment.setPort(ruleMap.get("PORT"));
        safetyEquipment.setEquipType(ruleMap.get("EQUIP_TYPE"));
        safetyEquipment.setOrgId(ruleMap.get("ORG_ID"));
        safetyEquipment.setEquipName(ruleMap.get("EQUIP_NAME"));
        safetyEquipment.setEquipDesc(ruleMap.get("EQUIP_DESC"));
        safetyEquipment.setAccount(ruleMap.get("ACCOUNT"));
        safetyEquipment.setPassword(ruleMap.get("PASSWORD"));
        safetyEquipment.setEquipState(Integer.valueOf(ruleMap.get("EQUIP_STATE")));
        safetyEquipment.setManager(ruleMap.get("MANAGER"));

        this.facility = ruleMap.get("facility");
        this.severity = ruleMap.get("severity");
        String mergeItems = ruleMap.get("MERGE_ITEMS");
        if (StrUtil.isNotEmpty(mergeItems)) {
            String[] split = mergeItems.split(",");
            this.mergeItemList = Arrays.asList(split);
        } else {
            this.mergeItemList = new ArrayList<>();
        }
        this.windowTime = Long.valueOf(ruleMap.get("WINDOW_TIME"));
        this.itemSplitSequence = ruleMap.get("ITEM_SPLIT");
        this.kvSplitSequence = ruleMap.get("KV_SPLIT");
        this.ipItemName = ruleMap.get("IP_ITEM_NAME");
        this.blockOffSecond = Long.valueOf(ruleMap.get("BLOCK_OFF_TIME"));
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * 获取当前安全设备在解析规则中的唯一Key
     *
     * @return key
     */
    public String getKey() {
        if (safetyEquipment != null && StrUtil.isNotBlank(safetyEquipment.getIp())) {
            if (StrUtil.isNotBlank(facility)) {
                if (StrUtil.isNotBlank(severity)) {
                    return safetyEquipment.getIp() + "~" + facility + "~" + severity;
                }
            }
        }
        return null;
    }

    /**
     * JSON格式化message
     *
     * @param msg 日志消息体
     * @return 格式化后的日志消息体
     */
    private HashMap<String, Object> getMessageInfoMap(String msg) {
        HashMap<String, Object> map = new HashMap<>();
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
        return map;
    }

    /**
     * 把日志根据归并字段写入到内存Map中
     *
     * @param log 原始日志对象
     */
    public void insertInCache(RsyslogNormalMessage log) {
        HashMap<String, Object> map = getMessageInfoMap(log.getMessage());

        StringBuilder unionKey = new StringBuilder();
        for (String mergeKey : this.mergeItemList) {
            unionKey.append(map.get(mergeKey)).append("~");
        }
        if (unionKey.length() > 0) {
            unionKey.delete(unionKey.length() - 1, unionKey.length());
        }
        cache.put(unionKey.toString(), log);
    }

    /**
     * 清空内存归并的日志缓存
     *
     * @return 复制出来的缓存日志
     */
    public synchronized HashMap<String, Object> flushCache() {
        HashMap<String, Object> map = new HashMap<>(cache.size());
        map.putAll(cache);
        cache.clear();
        return map;
    }

}
