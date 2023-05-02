package com.gem.loganalysis.facility;


import com.gem.loganalysis.model.bo.LogMergeRule;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;
import com.gem.loganalysis.util.LogFormatHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 防毒墙
 * Date: 2023/4/23 17:45
 *
 * @author GuoChao
 **/
public class PoisonProofWall extends AbstractFacility {

    private static final int FACILITY_ID = 2;

    private static PoisonProofWall INSTANCE;

    public PoisonProofWall() {
        // 根据配置初始化importKeys
        this.importantKeys = new ArrayList<>(Arrays.asList("dcountry", "dcity", "scountry", "scity", "event", "threat_type"));
        this.cache = new ConcurrentHashMap<>();
        this.logMergeRule = new LogMergeRule(true, importantKeys);
    }

    public static PoisonProofWall getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PoisonProofWall();
        }
        return INSTANCE;
    }

    @Override
    public HashMap<String, Object> parseMsgToJson(RsyslogNormalMessage log) {
        return LogFormatHandler.parseMsgToJson(log, this.importantKeys, ";", "=");
    }

    /**
     * 把数据存入缓存
     *
     * @param log 消息内容
     */
    @Override
    public void insertInCache(RsyslogNormalMessage log) {
        log.setLogType(FACILITY_ID);
        super.insertInCache(log);
    }

}
