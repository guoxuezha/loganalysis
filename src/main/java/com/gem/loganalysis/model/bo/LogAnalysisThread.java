package com.gem.loganalysis.model.bo;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 日志解析主线程
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/10 9:18
 */
@AllArgsConstructor
@Slf4j
public class LogAnalysisThread implements Runnable {

    private String record;

    private LogAnalysisRulePool logAnalysisRulePool;

    @Override
    public void run() {
        List<MergeLog> messageList = JSONUtil.toList("[" + record + "]", MergeLog.class);
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("1.监听到的 messageList.size = {}", messageList.size());
        for (MergeLog mergelog : messageList) {
            LogAnalysisRule logAnalysisRuleObject = logAnalysisRulePool.getLogAnalysisRuleObject(mergelog.getHost(), mergelog.getSeverity(), mergelog.getFacility());
            if (logAnalysisRuleObject != null) {
                logAnalysisRuleObject.printOverview();
                logAnalysisRuleObject.insertInCache(mergelog);
            }
        }
    }


}
