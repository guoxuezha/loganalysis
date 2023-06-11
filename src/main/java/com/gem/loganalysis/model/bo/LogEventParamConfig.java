package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 事件信息在日志中的参数配置
 * (即事件中的信息如何从日志范式中获取)
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/11 11:10
 */

public class LogEventParamConfig {

    private static LogEventParamConfig INSTANCE = new LogEventParamConfig();
    @Getter
    private String sourceIpItem;
    @Getter
    private String targetIpItem;
    @Getter
    private String eventTypeItem;
    @Getter
    private String riskLevelItem;

    private LogEventParamConfig() {
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, "SELECT FIELD_NAME, FIELD_TAG FROM SOP_LOG_NORMAL_FORM WHERE FIELD_TAG IS NOT NULL");
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                String fieldName = map.get("FIELD_NAME");
                switch (Integer.parseInt(map.get("FIELD_TAG"))) {
                    case 1:
                        sourceIpItem = fieldName;
                        break;
                    case 2:
                        targetIpItem = fieldName;
                        break;
                    case 3:
                        eventTypeItem = fieldName;
                        break;
                    case 4:
                        riskLevelItem = fieldName;
                        break;
                    default:
                }
            }
        }
    }

    public static LogEventParamConfig getInstance() {
        return INSTANCE;
    }

    public void refresh() {
        INSTANCE = new LogEventParamConfig();
    }

}
