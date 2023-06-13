package com.gem.loganalysis.model.bo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gem.loganalysis.mapper.AssetEventMapper;
import com.gem.loganalysis.mapper.AssetMergeLogMapper;
import com.gem.loganalysis.mapper.AssetRiskMapper;
import com.gem.loganalysis.mapper.LogAnalysisRuleMapper;
import com.gem.loganalysis.model.BaseConstant;
import com.gem.loganalysis.model.dto.edit.LogAnalysisRuleRelaDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.AssetEvent;
import com.gem.loganalysis.model.entity.AssetMergeLog;
import com.gem.loganalysis.model.entity.AssetRisk;
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

    /**
     * 执行数据操作用到的Mapper
     */
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

    private LogFormatter logFormatter;

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

        // 解析规则关联资产的基本信息
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

        this.logFormatter = new LogFormatter(ruleRelaId);

        this.blockFileDay = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String fileName = ruleRelaId + blockFileDay;
        this.blockFile = new BlockFile(getBlockFileRootPath(), fileName + ".DAT", fileName + ".IDX", true, 1, 64);
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

        // 若绑定的解析规则发生了变更,再执行关联修改
        this.logFormatter = new LogFormatter(ruleRelaId);
        this.enable = true;
    }

    public void printOverview() {
        log.info("LogAnalysisRule Overview: RULE_RELA_ID: {}, IP: {}, PORT: {}, FACILITY: {}, SEVERITY: {}, " +
                        "MERGE_ITEMS: {}", ruleRelaId, asset.getIpAddress(), asset.getServicePort(), facility, severity,
                mergeItemList.toArray());
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
        return String.format("enable : %s, CACHE COUNT: %d, CACHE LENGTH: %d Byte, queue.size: %d, logCount.size: %d"
                , enable, cacheMap.size(), ObjectSizeCalculator.getObjectSize(cacheMap), queue.size(),
                logCount.estimatedSize());
    }

    /**
     * 把日志根据归并字段写入到内存Map中
     *
     * @param mergeLog 原始日志对象
     */
    public void analysisSourceLog(MergeLog mergeLog) {
        // 1.将原始日志范式化
        LogNormalFormTree logMessageTree = logFormatter.format(mergeLog.getMessage());

        // 2.拼接构造业务联合主键
        StringBuilder unionKey = new StringBuilder();
        String unionKeyStr;
        if (CollUtil.isNotEmpty(this.mergeItemList)) {
            for (String mergeKey : this.mergeItemList) {
                unionKey.append(logMessageTree.getFieldValue(mergeKey)).append("~");
            }
            if (unionKey.length() > 0) {
                unionKey.delete(unionKey.length() - 1, unionKey.length());
            }
            unionKeyStr = unionKey.toString();
        } else {
            unionKeyStr = IdUtil.fastSimpleUUID();
        }
        mergeLog.setUnionKey(unionKeyStr);

        // 3.判断该日志是否需要作为当前归并周期的第一条生成logId
        MergeLog ifPresent = cacheMap.get(unionKeyStr);
        if (ifPresent == null) {
            mergeLog.generateLogId();
        } else {
            mergeLog.setLogId(ifPresent.getLogId());
        }

        // 4.将原始日志信息写入日志文件
        MergeLog sourceMergeLog = new MergeLog();
        BeanUtil.copyProperties(mergeLog, sourceMergeLog);
        BlockFileUtil.writeLog(sourceMergeLog, this);

        // 5.将日志对象写入归并缓存（异步生成归并日志）
        mergeLog.setMessage(logMessageTree.toJsonStr());
        MergeLog mLog = cacheMap.get(unionKeyStr);
        if (mLog == null) {
            cacheMap.put(unionKeyStr, mergeLog);
            mLog = mergeLog;
        }

        // 6.判断是否满足根据IP属地封堵条件
        LogEventParamConfig eventParamConfig = LogEventParamConfig.getInstance();
        String sourceIp = logMessageTree.getFieldValue(eventParamConfig.getSourceIpItem());
        if (BlockRuleServer.getInstance().judgeNeedRegionBlock(this.asset.getAssetId(), sourceIp)) {
            // 生成中危事件记录及风险记录
            eventStart(mergeLog, sourceIp, logMessageTree.getFieldValue(eventParamConfig.getTargetIpItem()), BaseConstant.EXTRA_TERRITORIAL_ACCESS, "2");
        } else {
            // 进入阈值计算分支
            logIncrAndStartEvent(mLog, unionKeyStr, logMessageTree);
        }
    }

    /**
     * 将取到的日志放入缓存并累加归并次数,再判断是否触发了事件
     *
     * @param mergeLog       归并日志对象
     * @param unionKey       业务联合主键
     * @param logMessageTree 日志消息结构树
     */
    private void logIncrAndStartEvent(MergeLog mergeLog, String unionKey, LogNormalFormTree logMessageTree) {
        mergeLog.getMergeCount().getAndIncrement();
        queue.add(mergeLog.toSimpleQueueMessageInfo());
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
        // 判断归并日志在时间窗口内的频率是否超过阈值
        if (StrUtil.isEmpty(logEventState.getIfPresent(unionKey)) && logCount.getIfPresent(unionKey).get() >= eventThreshold) {
            logEventState.put(unionKey, mergeLog.getLogId());

            LogEventParamConfig eventParamConfig = LogEventParamConfig.getInstance();
            String sourceIp = logMessageTree.getFieldValue(eventParamConfig.getSourceIpItem());
            String targetIp = logMessageTree.getFieldValue(eventParamConfig.getTargetIpItem());
            String eventType = logMessageTree.getFieldValue(eventParamConfig.getEventTypeItem());
            String riskLevel = logMessageTree.getFieldValue(eventParamConfig.getRiskLevelItem());
            log.info("{} 触发事件, 应执行封堵的IP为: {}, 攻击目标IP为: {}, 事件类型为: {}, 事件级别为: {}",
                    ruleRelaId, sourceIp, targetIp, eventType, riskLevel);
            if (eventType == null) {
                log.warn("发生未知类型事件! ruleRelaId: {}, mergeLog: {}", ruleRelaId, mergeLog);
                return;
            }
            // 事件开始
            eventStart(mergeLog, sourceIp, targetIp, eventType, riskLevel);
        }
    }

    /**
     * 扫描并停止事件
     */
    protected void scanAndStopEvent() {
        if (CollUtil.isEmpty(queue)) {
            return;
        }
        // 先从滑动窗口中移除早期日志记录
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
                log.info("{}: 周期滚动, Cache Flush 用时: {}, cacheData.size = {}", ruleRelaId, DateUtil.current() - start,
                        cacheData.size());
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
        if (this.mergeWindowTime == 0) {
            return 0;
        }
        int minute = DateUtil.hour(new Date(), true) * 60 + DateUtil.minute(new Date());
        return (minute / this.mergeWindowTime) + 1;
    }

    /**
     * 事件开始
     *
     * @param mergeLog  原始日志记录
     * @param sourceIp  事件源IP
     * @param targetIp  事件目标IP
     * @param eventType 事件类型
     * @param riskLevel 风险等级
     */
    private void eventStart(MergeLog mergeLog, String sourceIp, String targetIp, String eventType, String riskLevel) {
        String eventId = IdUtil.fastSimpleUUID();
        AssetEvent assetEvent = AssetEvent.builder()
                .eventId(eventId)
                .assetId(asset.getAssetId())
                .eventOrigin(1)
                .originId(mergeLog.getLogId())
                .sourceIp(sourceIp)
                .targetIp(targetIp)
                .eventType(eventType)
                .eventClass(riskLevel == null ? "未定义" : riskLevel)
                .beginTime(DatePattern.PURE_DATETIME_FORMAT.format(new Date()))
                .eventMessage(mergeLog.getMessage())
                .handleStatus(0)
                .build();
        int insertResult = assetEventMapper.insert(assetEvent);
        if (insertResult <= 0) {
            log.warn("事件创建失败! {}", mergeLog);
        }
        // 创建事件的同时新增风险记录(前提条件为风险等级不为空)
        if (riskLevel != null) {
            AssetRisk assetRisk = AssetRisk.builder()
                    .assetId(asset.getAssetId())
                    .vulnId(eventId)
                    .scanTime(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN))
                    .refEventId(eventId)
                    .riskLevel(Integer.parseInt(riskLevel))
                    .statusChangeTime(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN))
                    .build();
            int insert = assetRiskMapper.insert(assetRisk);
            if (insert <= 0) {
                log.warn("日志风险告警记录生成失败! {}", mergeLog);
            }
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
        if (updateResult < 0) {
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
