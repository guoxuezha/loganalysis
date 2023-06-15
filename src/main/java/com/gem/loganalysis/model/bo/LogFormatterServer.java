package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/13 11:25
 */
public class LogFormatterServer implements Serializable  {

    private static LogFormatterServer INSTANCE = new LogFormatterServer();

    /**
     * 日志范式属性英中文对照表
     */
    protected Map<String, String> itemKVMap = new HashMap<>();

    private LogFormatterServer() {
        init();
    }

    public static LogFormatterServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogFormatterServer();
        }
        return INSTANCE;
    }

    private void init() {
        DAO dao = new DAO();
        ArrayList<HashMap<String, String>> dataSet = dao.getDataSet(BaseConstant.DEFAULT_POOL_NAME, "SELECT FIELD_NAME, FIELD_DESC FROM SOP_LOG_NORMAL_FORM", 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                itemKVMap.put(map.get("FIELD_NAME"), map.get("FIELD_DESC"));
            }
        }
    }

    public void refresh() {
        init();
    }

}
