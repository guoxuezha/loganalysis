package com.gem.loganalysis.facility;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HashUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.loganalysis.model.bo.LogMergeRule;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 2023/4/26 16:46
 *
 * @author GuoChao
 **/
@Getter
public abstract class AbstractFacility implements Facility {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Setter
    protected List<String> importantKeys;

    @Setter
    protected LogMergeRule logMergeRule;

    protected ConcurrentHashMap<String, RsyslogNormalMessage> cache;

    @SneakyThrows
    @Override
    public void insertInCache(RsyslogNormalMessage log) {
        if (logMergeRule.isMerge() && CollUtil.isEmpty(logMergeRule.getMergeKeys())) {
            return;
        }
        HashMap<String, Object> map = parseMsgToJson(log);
        String messageContent = objectMapper.writeValueAsString(map);
        log.setMessage(messageContent);
        if (logMergeRule.isMerge()) {
            StringBuilder unionKey = new StringBuilder();
            for (String mergeKey : logMergeRule.getMergeKeys()) {
                unionKey.append(map.get(mergeKey)).append("~");
            }
            cache.put(unionKey.delete(unionKey.length() - 1, unionKey.length()) +
                    "&" + logMergeRule.getKeyFormat(), log);
        } else {
            cache.put(String.valueOf(HashUtil.fnvHash(messageContent)), log);
        }
    }

    @Override
    public synchronized HashMap<String, Object> flushCache() {
        HashMap<String, Object> map = new HashMap<>(cache.size());
        map.putAll(cache);
        cache.clear();
        return map;
    }

    @Override
    public String getCacheInfo() {
        return String.format("CACHE COUNT: %d, CACHE LENGTH: %d Byte", cache.mappingCount(), ObjectSizeCalculator.getObjectSize(cache));
    }

}
