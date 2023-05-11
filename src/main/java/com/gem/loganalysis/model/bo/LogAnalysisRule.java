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
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.mapper.AssetMergeLogMapper;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.util.SpringBeanUtil;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 安全设施业务类对象
 * Date: 2023/5/5 20:54
 *
 * @author GuoChao
 **/
@Data
@Slf4j
@Import(value = {AssetMergeLogMapper.class, AssetEventMapper.class})
public class LogAnalysisRule {

    private AssetEventMapper assetEventMapper;

    private AssetMergeLogMapper assetMergeLogMapper;

    /**
     * 归并前的日志记录
     */
    private Queue<SimpleQueueMessageInfo> queue;
    /**
     * 存放归并后的日志记录
     */
    private ConcurrentHashMap<String, MergeLog> cacheMap;

    /**
     * 事件滑窗内日志出现次数
     */
    private HashMap<String, AtomicInteger> logCount;

    /**
     * 日志事件状态,用于判断当前事件是否为新事件
     */
    private HashMap<String, String> logEventState;

    /**
     * 资产对象
     */
    private Asset asset;

    /**
     * 规则ID
     */
    private String ruleRelaId;

    /**
     * 日志生产子系统
     */
    private String facility;

    /**
     * 日志级别
     */
    private String severity;

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

    /**
     * 封堵时长
     */
    private Long blockOffSecond;

    public LogAnalysisRule() {
        this.queue = new LinkedBlockingQueue<>();
        this.cacheMap = new ConcurrentHashMap<>();
        this.logCount = new HashMap<>();
        this.logEventState = new HashMap<>();
    }

    public LogAnalysisRule(HashMap<String, Object> ruleMap) {
        this.ruleRelaId = (String) ruleMap.get("RULE_RELA_ID");

        this.queue = new LinkedBlockingQueue<>();
        this.cacheMap = new ConcurrentHashMap<>();
        this.logCount = new HashMap<>();
        this.logEventState = new HashMap<>();
        this.nextMergeWindowStartTime = new Date();
        this.assetEventMapper = SpringBeanUtil.getBean(AssetEventMapper.class);
        this.assetMergeLogMapper = SpringBeanUtil.getBean(AssetMergeLogMapper.class);

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

        ruleMap.get("RULE_TYPE");
        ruleMap.get("METHOD_NAME");
        this.itemSplitSequence = (String) ruleMap.get("ITEM_SPLIT");
        this.kvSplitSequence = (String) ruleMap.get("KV_SPLIT");
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
        return String.format("CACHE COUNT: %d, CACHE LENGTH: %d Byte", cacheMap.mappingCount(), ObjectSizeCalculator.getObjectSize(cacheMap));
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

        //log.info("{} 2. 日志业务联合主键为: {}", ruleRelaId, unionKey);
        // 判断该日志是否需要作为当前归并周期的第一条生成logId
        if (new Date().after(this.nextMergeWindowStartTime)) {
            log.info("{} *** 周期滚动, Cache Flush! ", ruleRelaId);
            this.nextMergeWindowStartTime = DateUtil.offsetMinute(this.nextMergeWindowStartTime, this.mergeWindowTime);
            HashMap<String, Object> cacheData = flushCache();
            log.info("{} *** 周期滚动 cacheData.size = {}", ruleRelaId, cacheData.size());
            // 执行入库
            for (Map.Entry<String, Object> entry : cacheData.entrySet()) {
                try {
                    insertMergeLog(entry.getKey(), (MergeLog) entry.getValue());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

        if (cacheMap.get(unionKeyStr) == null) {
            mergeLog.generateLogId();
        }

        // 执行内存计算操作
        queue.add(mergeLog.toSimpleQueueMessageInfo());
        // 计算归并日志在归并周期内的出现次数,进行 Put + 1
        cacheMap.putIfAbsent(unionKeyStr, mergeLog);
        cacheMap.get(unionKeyStr).getMergeCount().getAndIncrement();
        logCount.putIfAbsent(unionKeyStr, new AtomicInteger(0));
        logCount.get(unionKeyStr).getAndIncrement();
        //log.info("{} 3.日志归并, unionKey = {}, count = {} ", ruleRelaId, unionKey, logCount.get(unionKeyStr).get());

        // TODO: 将日志信息放入文件写入队列

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
     * 扫描并启动事件
     */
    protected void scanAndStartEvent() {
        for (Map.Entry<String, AtomicInteger> entry : logCount.entrySet()) {
            String unionKey = entry.getKey();
            if (logEventState.get(unionKey) == null && entry.getValue().get() > eventThreshold) {
                logEventState.put(unionKey, cacheMap.get(unionKey).getLogId());
                JSONObject jsonObject = JSONUtil.parseObj(cacheMap.get(unionKey).getMessage());
                log.info("{} 5.触发事件, unionKey = {}, 应执行封堵的IP为: {}", ruleRelaId, unionKey, jsonObject.get(eventKeyWord));
                eventStart(cacheMap.get(unionKey));
            }
        }
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
                logCount.get(unionKey).getAndDecrement();
                //log.info("{} 4.从滑窗移除早期记录, unionKey = {}, logRecordTimeStamp = {}, count = {}", ruleRelaId, unionKey, message.getTimeStamp().toString(), logCount.get(unionKey).get());
            } else {
                break;
            }
        }

        // 再判断是否存在可终止的事件
        for (Map.Entry<String, AtomicInteger> entry : logCount.entrySet()) {
            String unionKey = entry.getKey();
            if (logEventState.get(unionKey) != null && entry.getValue().get() < eventThreshold) {
                log.info("{} 5.事件结束, unionKey = {}", ruleRelaId, unionKey);
                logEventState.put(unionKey, null);
                eventEnd(logEventState.get(unionKey));
            }
        }
    }

    /**
     * 清空内存归并的日志缓存
     *
     * @return 复制出来的缓存日志
     */
    public synchronized HashMap<String, Object> flushCache() {
        long start = DateUtil.current();
        HashMap<String, Object> map = new HashMap<>(cacheMap.size());
        map.putAll(cacheMap);
        cacheMap.clear();
        long l = DateUtil.current() - start;
        if (l > 0) {
            log.info("{}: Cache Flush 用时: {}", this.getKey(), l);
        }
        return map;
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
        int insertResult = assetMergeLogMapper.insert(assetMergeLog);
        if (insertResult <= 0) {
            log.warn("归并日志新增失败! {}", mergeLog);
        }
    }

    /**
     * 事件开始
     *
     * @param mergeLog 原始日志记录
     */
    private void eventStart(MergeLog mergeLog) {
        AssetEvent assetEvent = AssetEvent.builder()
                .eventId(IdUtil.fastSimpleUUID())
                .assetId(asset.getAssetId())
                .eventOrigin(1)
                .originId(mergeLog.getLogId())
                .beginTime(DatePattern.PURE_DATETIME_FORMAT.format(new Date()))
                .eventMessage(mergeLog.getMessage())
                .handleStatus(0)
                .build();
        int insertResult = assetEventMapper.insert(assetEvent);
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
        AssetEvent assetEvent = AssetEvent.builder()
                .endTime(DatePattern.PURE_DATETIME_FORMAT.format(new Date()))
                .build();
        LambdaQueryWrapper<AssetEvent> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(AssetEvent::getEventOrigin, 1)
                .eq(AssetEvent::getOriginId, mergeLogId);
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
