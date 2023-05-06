package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import com.gem.loganalysis.mapper.SafetyEquipmentMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Description: 安全设备池
 * Date: 2023/5/5 21:14
 *
 * @author GuoChao
 **/
@Component
public class SafetyEquipmentPool {

    //private static SafetyEquipmentPool INSTANCE;
    private final HashMap<String, SafetyEquipmentBO> safetyEquipmentBoMap;
    @Resource
    private SafetyEquipmentMapper safetyEquipmentMapper;

    private SafetyEquipmentPool() {
        safetyEquipmentBoMap = new HashMap<>();
    }

    //public static SafetyEquipmentPool getInstance() {
    //    if (INSTANCE == null) {
    //        INSTANCE = new SafetyEquipmentPool();
    //    }
    //    return INSTANCE;
    //}

    /**
     * 根据三项信息查询
     * @param ip
     * @param facility
     * @param severity
     * @return
     */
    public SafetyEquipmentBO getSafetyEquipmentBO(String ip, String facility, String severity) {
        String key = ip + "~" + facility + "~" + severity;
        if (safetyEquipmentBoMap.get(key) == null) {
            loadSafetyEquipmentBO(key);
        }
        return safetyEquipmentBoMap.get(key);
    }

    /**
     * 加载安全设备信息
     *
     * @param key 业务唯一联合主键
     */
    private void loadSafetyEquipmentBO(String key) {
        String[] split = key.split("~");
        HashMap<String, String> ruleMap = safetyEquipmentMapper.getEquipAnalysisAndBlockRule(split[0], split[1], split[2]);
        if (CollUtil.isNotEmpty(ruleMap)) {
            SafetyEquipmentBO equipmentBO = new SafetyEquipmentBO(ruleMap);
            safetyEquipmentBoMap.put(key, equipmentBO);
        }
    }


}
