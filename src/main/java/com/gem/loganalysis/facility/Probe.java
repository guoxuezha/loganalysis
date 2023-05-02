package com.gem.loganalysis.facility;


import com.gem.loganalysis.model.bo.LogMergeRule;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import com.gem.loganalysis.util.LogFormatHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 探针
 * Date: 2023/4/23 17:45
 *
 * @author GuoChao
 **/
public class Probe extends AbstractFacility {

    private static final int FACILITY_ID = 1;

    private static Probe INSTANCE;

    public Probe() {
        // 根据配置初始化importKeys 日志分析
        this.importantKeys = new ArrayList<>(Arrays.asList("src_addr", "src_port", "dst_addr", "dst_port"));
        this.cache = new ConcurrentHashMap<>();
        this.logMergeRule = new LogMergeRule(true, importantKeys);
    }

    public static Probe getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Probe();
        }
        return INSTANCE;
    }

    @Override
    public HashMap<String, Object> parseMsgToJson(RsyslogNormalMessage log) {
        return LogFormatHandler.parseMsgToJson(log, this.importantKeys, ";", ":");
    }

    @Override
    public void insertInCache(RsyslogNormalMessage log) {
        log.setLogType(FACILITY_ID);
        super.insertInCache(log);
    }

}
