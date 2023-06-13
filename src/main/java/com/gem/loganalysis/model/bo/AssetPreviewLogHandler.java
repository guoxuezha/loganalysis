package com.gem.loganalysis.model.bo;

import cn.hutool.core.collection.CollUtil;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.loganalysis.model.BaseConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 资产预览日志处理类
 * 维护已有资产各类日志已接收的样例日志数量
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/6/9 9:56
 */
public class AssetPreviewLogHandler {

    private static AssetPreviewLogHandler INSTANCE = null;
    /**
     * 各类日志已保存的预览日志
     */
    private final ConcurrentHashMap<String, AtomicInteger> previewLogNumMap;

    private AssetPreviewLogHandler() {
        previewLogNumMap = new ConcurrentHashMap<>();
        init();
    }

    public static AssetPreviewLogHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AssetPreviewLogHandler();
        }
        return INSTANCE;
    }

    /**
     * 从数据库初始化样例日志条目情况
     */
    private void init() {
        String querySql = "SELECT HOST, SEVERITY, FACILITY, COUNT(1) AS TOTAL FROM SOP_ASSET_LOG_PREVIEW GROUP BY HOST, SEVERITY, FACILITY";
        ArrayList<HashMap<String, String>> dataSet = new DAO().getDataSet(BaseConstant.DEFAULT_POOL_NAME, querySql, 0, 0);
        if (CollUtil.isNotEmpty(dataSet)) {
            for (HashMap<String, String> map : dataSet) {
                String key = map.get("HOST") + map.get("SEVERITY") + map.get("FACILITY");
                int num = Integer.parseInt(map.get("TOTAL"));
                previewLogNumMap.put(key, new AtomicInteger(num));
            }
        }
    }

    /**
     * 追加样例原始日志
     *
     * @param mergeLog 日志对象
     */
    public void append(MergeLog mergeLog) {
        String key = mergeLog.getHost() + mergeLog.getSeverity() + mergeLog.getFacility();
        if (previewLogNumMap.get(key) == null) {
            previewLogNumMap.put(key, new AtomicInteger(0));
        }
        if (previewLogNumMap.get(key).get() < 10) {
            String insertSql = String.format("INSERT INTO `SOP_ASSET_LOG_PREVIEW` (`HOST`, `SEVERITY`, `FACILITY`, `TIMESTAMP`, `MESSAGE`, `TAG`) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", mergeLog.getHost(), mergeLog.getSeverity(), mergeLog.getFacility(), mergeLog.getTimestamp(), mergeLog.getMessage(), mergeLog.getTag());
            new DAO().execCommand(BaseConstant.DEFAULT_POOL_NAME, insertSql);
            previewLogNumMap.get(key).incrementAndGet();
        }
    }

}
