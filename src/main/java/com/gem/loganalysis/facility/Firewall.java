package com.gem.loganalysis.facility;


import com.gem.loganalysis.model.bo.RsyslogNormalMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 2023/4/23 17:44
 *
 * @author GuoChao
 **/
public class Firewall extends AbstractFacility {

    private static final int FACILITY_ID = 3;

    private static Firewall INSTANCE;

    private final List<String> IMPORTANT_KEYS;

    private Firewall() {
        this.IMPORTANT_KEYS = new ArrayList<>();
        this.cache = new ConcurrentHashMap<>();
    }

    public static Firewall getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Firewall();
        }
        return INSTANCE;
    }

    @Override
    public HashMap<String, Object> parseMsgToJson(RsyslogNormalMessage log) {
        return null;
    }


    @Override
    public void insertInCache(RsyslogNormalMessage log) {

    }

    @Override
    public HashMap<String, Object> flushCache() {
        return new HashMap<>(2);
    }
}
