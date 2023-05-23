package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.mapper.AssetMergeLogMapper;
import com.gem.loganalysis.mapper.AssetRiskMapper;
import com.gem.loganalysis.mapper.LogAnalysisRuleMapper;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleRelaDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.model.entity.AssetRisk;
import com.gem.loganalysis.model.vo.AssetAnalysisRuleVO;
import com.gem.loganalysis.util.BlockFileUtil;
import com.gem.loganalysis.util.SpringContextUtil;
import com.gem.utils.file.BlockFile;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gem.loganalysis.util.BlockFileUtil.getBlockFileRootPath;

/**
 * Description: 安全设施业务类对象
 * Date: 2023/5/5 20:54
 *
 * @author GuoChao
 **/
@Slf4j
public class LogAnalysisRuleBo {

    /**
     * 存放归并后的日志记录
     */
    private final ConcurrentHashMap<String, MergeLog> cacheMap;

    /**
     * 归并前的日志记录
     */
    private final Queue<SimpleQueueMessageInfo> queue;

    /**
     * 事件滑窗内日志出现次数
     */
    private final Cache<String, AtomicInteger> logCount;

    /**
     * 日志事件状态,用于判断当前事件是否为新事件
     */
    private final Cache<String, String> logEventState;
    private final AssetEventMapper assetEventMapper;
    private final AssetRiskMapper assetRiskMapper;
    private final AssetMergeLogMapper assetMergeLogMapper;
    private final LogAnalysisRuleMapper logAnalysisRuleMapper;
    /**
     * 资产对象
     */
    private final Asset asset;
    /**
     * 规则ID
     */
    @Getter
    private final String ruleRelaId;
    private final String analysisRuleId;

    /**
     * 全量日志快文件
     */
    @Getter
    @Setter
    private BlockFile blockFile;

    @Getter
    @Setter
    private String blockFileDay;

    /**
     * 可用状态
     */
    private boolean enable;
    /**
     * 日志生产子系统
     */
    private String facility;
    /**
     * 日志级别
     */
    private String severity;
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
     * 归并字段列表
     */
    private List<String> mergeItemList;
    /**
     * 归并窗口时长(分钟)
     */
    private Integer mergeWindowTime;
    /**
     * 下一个归并窗口(周期)的开始时间
     */
    private Date nextMergeWindowStartTime;
    /**
     * 事件统计滑动窗口时长(分钟)
     */
    private Integer eventWindowTime;
    /**
     * 事件判定阈值
     */
    private Integer eventThreshold;
    /**
     * 关注的IP字段名
     */
    private String eventKeyWord;

    private String eventTypeItem;

    private String eventClassItem;

    /**
     * 封堵时长
     */
    private Long blockOffSecond;

    public LogAnalysisRuleBo(HashMap<String, Object> ruleMap) {
        this.ruleRelaId = (String) ruleMap.get("RULE_RELA_ID");

        this.queue = new LinkedBlockingQueue<>();
        this.cacheMap = new ConcurrentHashMap<>();
        this.logCount = Caffeine.newBuilder().maximumSize(10000).build();
        this.logEventState = Caffeine.newBuilder().maximumSize(10000).build();
        this.nextMergeWindowStartTime = new Date();
        this.assetEventMapper = SpringContextUtil.getBean(AssetEventMapper.class);
        this.assetRiskMapper = SpringContextUtil.getBean(AssetRiskMapper.class);
        this.assetMergeLogMapper = SpringContextUtil.getBean(AssetMergeLogMapper.class);
        this.logAnalysisRuleMapper = SpringContextUtil.getBean(LogAnalysisRuleMapper.class);

        this.asset = new Asset();
        this.asset.setAssetId((String) ruleMap.get("ASSET_ID"));
        this.asset.setAssetName((String) ruleMap.get("ASSET_NAME"));
        this.asset.setAssetClass((String) ruleMap.get("ASSET_CLASS"));
        this.asset.setAssetType((String) ruleMap.get("ASSET_TYPE"));
        this.asset.setIpAddress((String) ruleMap.get("IP_ADDRESS"));
        /*this.asset.setServicePort(Integer.valueOf(ruleMap.get("SERVICE_PORT")));
        this.asset.setNmPort(Integer.valueOf(ruleMap.get("NM_PORT")));
        this.asset.setNmProcotol(ruleMap.get("NM_PROCOTOL"));
        this.asset.setNmAccount(ruleMap.get("NM_ACCOUNT"));
        this.asset.setNmPassword(ruleMap.get("NM_PASSWORD"));
        this.asset.setAssetOrg(ruleMap.get("ASSET_ORG"));
        this.asset.setAssetManager(ruleMap.get("ASSET_MANAGER"));*/

        this.analysisRuleId = (String) ruleMap.get("ANALYSIS_RULE_ID");
        this.facility = (String) ruleMap.get("FACILITY");
        this.severity = (String) ruleMap.get("SEVERITY");
        String mergeItems = (String) ruleMap.get("MERGE_ITEMS");
        if (StrUtil.isNotEmpty(mergeItems)) {
            String[] split = mergeItems.split(",");
            this.mergeItemList = Arrays.asList(split);
        } else {
            this.mergeItemList = new ArrayList<>();
        }
        this.mergeWindowTime = (Integer) ruleMap.get("MERGE_WINDOW_TIME");
        this.eventWindowTime = (Integer) ruleMap.get("EVENT_WINDOW_TIME");
        this.eventThreshold = (Integer) ruleMap.get("EVENT_THRESHOLD");
        this.eventKeyWord = (String) ruleMap.get("EVENT_KEYWORD");
        this.eventTypeItem = (String) ruleMap.get("EVENT_TYPE_ITEM");
        this.eventClassItem = (String) ruleMap.get("EVENT_CLASS_ITEM");

        this.ruleType = (Integer) ruleMap.get("RULE_TYPE");
        this.itemSplitSequence = (String) ruleMap.get("ITEM_SPLIT");
        this.kvSplitSequence = (String) ruleMap.get("KV_SPLIT");
        this.jarName = (String) ruleMap.get("JAR_NAME");
        this.version = (String) ruleMap.get("VERSION");
        this.methodName = (String) ruleMap.get("METHOD_NAME");

        this.blockFileDay = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String fileName = ruleRelaId + blockFileDay;
        this.blockFile = new BlockFile(getBlockFileRootPath(), fileName + ".DAT", fileName + ".IDX", true, 3, 64);
        this.enable = true;
    }

    public boolean isEnable() {
        return this.enable;
    }

    /**
     * 重新加载解析规则信息
     *
     * @param dto 新对象
     */
    public void reloadInfo(LogAnalysisRuleRelaDTO dto) {
        this.enable = false;
        this.facility = dto.getFacility();
        this.severity = dto.getSeverity();
        if (StrUtil.isNotEmpty(dto.getMergeItems())) {
            String[] split = dto.getMergeItems().split(",");
            this.mergeItemList = Arrays.asList(split);
        }
        this.mergeWindowTime = dto.getMergeWindowTime();
        this.eventWindowTime = dto.getEventWindowTime();
        this.eventThreshold = dto.getEventThreshold();
        this.eventKeyWord = dto.getEventKeyword();
        this.eventTypeItem = dto.getEventTypeItem();
        this.eventClassItem = dto.getEventClassItem();

        // 若绑定的解析规则发生了变更,再执行关联修改
        if (!this.analysisRuleId.equals(dto.getAnalysisRuleId())) {
            AssetAnalysisRuleVO analysisRule = logAnalysisRuleMapper.getAnalysisRuleVOById(dto.getAnalysisRuleId());
            this.ruleType = analysisRule.getRuleType();
            this.itemSplitSequence = analysisRule.getItemSplit();
            this.kvSplitSequence = analysisRule.getKvSplit();
            this.jarName = analysisRule.getJarName();
            this.version = analysisRule.getVersion();
            this.methodName = analysisRule.getMethodName();
        }
        this.enable = true;
    }

    public void printOverview() {
        log.info(" LogAnalysisRule Overview: RULE_RELA_ID: {}, IP: {}, PORT: {}, FACILITY: {}, SEVERITY: {}, MERGE_ITEMS: {}", ruleRelaId, asset.getIpAddress(), asset.getServicePort(), facility, severity, mergeItemList.toArray());
    }

    /**
     * 获取当前安全设备在解析规则中的唯一Key
     *
     * @return key
     */
    public String getKey() {
        if (asset != null && StrUtil.isNotBlank(asset.getIpAddress())) {
            if (StrUtil.isNotBlank(facility)) {
                if (StrUtil.isNotBlank(severity)) {
                    return asset.getIpAddress() + "~" + facility + "~" + severity;
                }
            }
        }
        return null;
    }

    public String getCacheInfo() {
        return String.format("enable : %s, CACHE COUNT: %d, CACHE LENGTH: %d Byte, queue.size: %d, logCount.size: %d", enable, cacheMap.size(), ObjectSizeCalculator.getObjectSize(cacheMap), queue.size(), logCount.estimatedSize());
    }

    /**
     * 把日志根据归并字段写入到内存Map中
     *
     * @param mergeLog 原始日志对象
     */
    public void insertInCache(MergeLog mergeLog) {
        HashMap<String, Object> map = getMessageInfoMap(mergeLog.getMessage());
        mergeLog.setMessage(JSONUtil.toJsonStr(map));

        // 拼接构造业务联合主键
        StringBuilder unionKey = new StringBuilder();
        for (String mergeKey : this.mergeItemList) {
            unionKey.append(map.get(mergeKey)).append("~");
        }
        if (unionKey.length() > 0) {
            unionKey.delete(unionKey.length() - 1, unionKey.length());
        }
        String unionKeyStr = unionKey.toString();
        mergeLog.setUnionKey(unionKeyStr);

        // 判断该日志是否需要作为当前归并周期的第一条生成logId
        MergeLog ifPresent = cacheMap.get(unionKeyStr);
        if (ifPresent == null) {
            mergeLog.generateLogId();
        } else {
            mergeLog.setLogId(ifPresent.getLogId());
        }

        queue.add(mergeLog.toSimpleQueueMessageInfo());

        logIncrAndStartEvent(mergeLog, unionKeyStr);

        // 将日志信息放入文件写入日志文件
        BlockFileUtil.writeLog(mergeLog, this);
    }

    /**
     * 将取到的日志放入缓存并累加归并次数,再判断是否触发了事件
     *
     * @param mergeLog 归并日志对象
     * @param unionKey 业务联合主键
     */
    private void logIncrAndStartEvent(MergeLog mergeLog, String unionKey) {
        MergeLog eventStartLog = null;
        JSONObject jsonObject = null;
        synchronized (cacheMap) {
            MergeLog mLog = cacheMap.get(unionKey);
            if (mLog == null) {
                cacheMap.put(unionKey, mergeLog);
                mLog = mergeLog;
            }
            // 归并周期日志出现的次数
            mLog.getMergeCount().getAndIncrement();


            if (logCount.getIfPresent(unionKey) == null) {
                logCount.put(unionKey, new AtomicInteger(0));
            }
            logCount.getIfPresent(unionKey).getAndIncrement();
            // 若cacheFlush和eventScan均采用定时任务的方式执行,可能导致扫描到的事件涉及到的归并日志被Flush掉,从而丢失ORIGIN_ID信息的问题,故事件发生的检查应立即执行
            for (SimpleQueueMessageInfo message : queue) {
                long between = DateUtil.between(message.getTimeStamp(), DateUtil.date(), DateUnit.MINUTE);
                if (between > eventWindowTime) {
                    queue.poll();
                    logCount.getIfPresent(unionKey).getAndDecrement();
                } else {
                    break;
                }
            }
            if (StrUtil.isEmpty(logEventState.getIfPresent(unionKey)) && logCount.getIfPresent(unionKey).get() > eventThreshold) {
                logEventState.put(unionKey, mLog.getLogId());
                jsonObject = JSONUtil.parseObj(mLog.getMessage());
                log.info("{} 触发事件, unionKey = {}, 应执行封堵的IP为: {}, 事件类型为: {}, 事件级别为: {}", ruleRelaId, unionKey,
                        jsonObject.get(eventKeyWord), jsonObject.get(eventTypeItem), jsonObject.get(eventClassItem));
                eventStartLog = mLog;
            }
        }
        if (eventStartLog != null) {
            eventStart(eventStartLog, jsonObject);
        }
    }

    /**
     * JSON格式化message
     *
     * @param msg 日志消息体
     * @return 格式化后的日志消息体
     */
    private HashMap<String, Object> getMessageInfoMap(String msg) {
        HashMap<String, Object> map = new HashMap<>();
        if (this.ruleType == 1) {
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

    /**
     * 扫描并停止事件
     */
    protected void scanAndStopEvent() {
        // 先从滑动窗口中移除早期日志记录
        if (CollUtil.isEmpty(queue)) {
            return;
        }
        for (SimpleQueueMessageInfo message : queue) {
            long between = DateUtil.between(message.getTimeStamp(), DateUtil.date(), DateUnit.MINUTE);
            if (between > eventWindowTime) {
                String unionKey = message.getUnionKey();
                queue.poll();
                if (logCount.getIfPresent(unionKey) != null) {
                    logCount.getIfPresent(unionKey).getAndDecrement();
                }
            } else {
                break;
            }
        }

        // 再判断是否存在可终止的事件
        for (Map.Entry<String, AtomicInteger> entry : logCount.asMap().entrySet()) {
            String unionKey = entry.getKey();
            if (StrUtil.isNotEmpty(logEventState.getIfPresent(unionKey)) && entry.getValue().get() < eventThreshold) {
                log.info("{} 事件结束, unionKey = {}", ruleRelaId, unionKey);
                eventEnd(logEventState.getIfPresent(unionKey));
                logEventState.put(unionKey, "");
            }
        }
    }

    /**
     * 清空内存归并的日志缓存,并将该部分日志批量写入数据库
     */
    public void flushCache() {
        if (new Date().after(this.nextMergeWindowStartTime)) {
            this.nextMergeWindowStartTime = DateUtil.offsetMinute(this.nextMergeWindowStartTime, this.mergeWindowTime);
            HashMap<String, Object> cacheData;
            synchronized (cacheMap) {
                long start = DateUtil.current();
                cacheData = new HashMap<>(cacheMap.size());
                cacheData.putAll(cacheMap);
                cacheMap.clear();
                log.info("{}: 周期滚动, Cache Flush 用时: {}, cacheData.size = {}", ruleRelaId, DateUtil.current() - start, cacheData.size());
            }
            // 执行入库
            for (Map.Entry<String, Object> entry : cacheData.entrySet()) {
                try {
                    insertMergeLog(entry.getKey(), (MergeLog) entry.getValue());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 执行日志内容新增操作
     *
     * @param key      消息体联合主键
     * @param mergeLog 消息内容
     */
    private void insertMergeLog(String key, MergeLog mergeLog) {
        AssetMergeLog assetMergeLog = new AssetMergeLog();
        assetMergeLog.setLogId(mergeLog.getLogId());
        assetMergeLog.setRuleRelaId(ruleRelaId);
        assetMergeLog.setUnionKey(key);
        assetMergeLog.setLogPeriod(0);
        assetMergeLog.setEventCount(mergeLog.getMergeCount().get());
        assetMergeLog.setUpdateTime(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT));
        assetMergeLog.setMessage(mergeLog.getMessage());
        assetMergeLog.setTag(mergeLog.getTag());
        // 计算归并窗口时序点
        assetMergeLog.setLogPeriod(getLogPeriodOfDay());
        int insertResult = assetMergeLogMapper.insert(assetMergeLog);
        if (insertResult <= 0) {
            log.warn("归并日志新增失败! {}", mergeLog);
        }
    }

    private int getLogPeriodOfDay() {
        int minute = DateUtil.hour(new Date(), true) * 60 + DateUtil.minute(new Date());
        return (minute / this.mergeWindowTime) + 1;
    }

    /**
     * 事件开始
     *
     * @param mergeLog   原始日志记录
     * @param jsonObject 日志内容对象
     */
    private void eventStart(MergeLog mergeLog, JSONObject jsonObject) {
        String eventId = IdUtil.fastSimpleUUID();
        AssetEvent assetEvent = AssetEvent.builder()
                .eventId(eventId)
                .assetId(asset.getAssetId())
                .eventOrigin(1)
                .originId(mergeLog.getLogId())
                .eventType(jsonObject.get(eventTypeItem) != null ? (String) jsonObject.get(eventTypeItem) : "未定义")
                .eventClass(jsonObject.get(eventClassItem) != null ? (String) jsonObject.get(eventClassItem) : "未定义")
                .sourceIp((String) jsonObject.get(eventKeyWord))
                .beginTime(DatePattern.PURE_DATETIME_FORMAT.format(new Date()))
                .eventMessage(mergeLog.getMessage())
                .handleStatus(0)
                .build();
        int insertResult = assetEventMapper.insert(assetEvent);
        // 创建事件的同时新增风险记录
        AssetRisk assetRisk = AssetRisk.builder()
                .assetId(asset.getAssetId())
                .vulnId(eventId)
                .scanTime(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN))
                .refEventId(eventId)
                .statusChangeTime(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN))
                .build();
        assetRiskMapper.insert(assetRisk);
        if (insertResult <= 0) {
            log.warn("事件创建失败! {}", mergeLog);
        }
    }

    /**
     * 事件结束
     *
     * @param mergeLogId 要修改的归并日志ID
     */
    private void eventEnd(String mergeLogId) {
        if (mergeLogId == null) {
            log.warn("缓存中的logId被删除！");
            return;
        }
        AssetEvent assetEvent = AssetEvent.builder().endTime(DatePattern.PURE_DATETIME_FORMAT.format(new Date())).build();
        LambdaQueryWrapper<AssetEvent> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(AssetEvent::getEventOrigin, 1).eq(AssetEvent::getOriginId, mergeLogId);
        int updateResult = assetEventMapper.update(assetEvent, updateWrapper);
        if (updateResult <= 0) {
            log.warn("MergeLogId为 {} 的事件结束时间修改失败!", mergeLogId);
        }
    }

    @Data
    protected static class SimpleQueueMessageInfo {

        private String unionKey;

        private DateTime timeStamp;

        public SimpleQueueMessageInfo(String unionKey, String timeStamp) {
            this.unionKey = unionKey;
            String replace = timeStamp.split("\\.")[0].replace("T", " ");
            this.timeStamp = DateUtil.parse(replace, DatePattern.NORM_DATETIME_FORMAT);
        }

    }


}
