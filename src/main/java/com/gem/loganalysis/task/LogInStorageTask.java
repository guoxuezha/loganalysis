package com.gem.loganalysis.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gem.loganalysis.facility.AbstractFacility;
import com.gem.loganalysis.facility.Facility;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import com.gem.loganalysis.model.entity.Log;
import com.gem.loganalysis.model.entity.LogIndex;
import com.gem.loganalysis.service.LogIndexService;
import com.gem.loganalysis.service.LogService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description: 缓存日志
 * Date: 2023/4/25 19:45
 *
 * @author GuoChao
 **/
@Component
@Slf4j
public class LogInStorageTask {

    @Getter
    private final Set<Facility> facilitySetInstance = new HashSet<>();

    @Resource
    private LogService logService;

    @Resource
    private LogIndexService logIndexService;

    /**
     * 自动装载特征设施类
     *
     * @return 识别到的特征设施类名
     */
    private void loadFacilitySet() {
        Set<Class<?>> classes = ClassUtil.scanPackage("com.gem.loganalysis.facility");
        String superClassName = AbstractFacility.class.getName();
        for (Class<?> clazz : classes) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && superClassName.equals(superclass.getName())) {
                for (Method method : clazz.getMethods()) {
                    if ("getInstance".equals(method.getName())) {
                        Facility instance = ReflectUtil.invoke(clazz, method);
                        facilitySetInstance.add(instance);
                    }
                }
            }
        }
    }

    /**
     * 每3分钟执行一次缓存释放
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void loadInStorage() {
        if (facilitySetInstance.size() == 0) {
            loadFacilitySet();
        }
        for (Facility facility : facilitySetInstance) {
            long start = DateUtil.current();
            HashMap<String, Object> map = facility.flushCache();
            long l = DateUtil.current() - start;
            if (l > 0) {
                log.info("{}: Cache Flush 用时: {}", facility.getClass().getSimpleName(), l);
            }
            // 执行入库
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    insertOrUpdateLog(entry.getKey(), (RsyslogNormalMessage) entry.getValue());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 执行日志内容新增或修改操作
     *
     * @param key     消息体联合主键
     * @param logInfo 消息内容
     */
    private void insertOrUpdateLog(String key, RsyslogNormalMessage logInfo) {
        String unionKey;
        String keyFormat;
        if (key.contains("&")) {
            String[] split = key.split("&");
            unionKey = split[0];
            keyFormat = split[1];
            // 先查询log_index,判断应执行新增还是修改
            LambdaQueryWrapper<LogIndex> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LogIndex::getLogType, 2)
                    .eq(LogIndex::getKeyFormat, keyFormat)
                    .eq(LogIndex::getUnionKey, unionKey);
            LogIndex logIndex = logIndexService.getOne(queryWrapper);

            Log l = new Log(logInfo);
            if (logIndex == null) {
                if (logService.save(l)) {
                    logIndexService.save(new LogIndex(l.getLogType(), keyFormat, unionKey, l.getLogId()));
                }
            } else {
                l.setLogId(logIndex.getLogId());
                logService.updateById(l);
            }
        } else {
            logService.save(new Log(logInfo));
        }
    }

}
