package com.gem.loganalysis.facility;

import com.gem.loganalysis.model.bo.LogMergeRule;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import com.gem.loganalysis.util.LogFormatHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 2023/4/23 17:45
 *
 * @author GuoChao
 **/
public class Honeypot extends AbstractFacility {

    private static final int FACILITY_ID = 4;

    private static Honeypot INSTANCE;

    private final List<String> IMPORTANT_KEYS;

    private Honeypot() {
        this.IMPORTANT_KEYS = new ArrayList<>();
        this.cache = new ConcurrentHashMap<>();
        this.logMergeRule = new LogMergeRule(false, null);
    }

    public static Honeypot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Honeypot();
        }
        return INSTANCE;
    }

    @Override
    public HashMap<String, Object> parseMsgToJson(RsyslogNormalMessage log) {
        return LogFormatHandler.parseMsgToJson(log, this.importantKeys, ",", ":");

    }

    @Override
    public void insertInCache(RsyslogNormalMessage log) {
        log.setLogType(FACILITY_ID);
        super.insertInCache(log);
    }

    @Override
    public HashMap<String, Object> flushCache() {
        return new HashMap<>(2);
    }
}
