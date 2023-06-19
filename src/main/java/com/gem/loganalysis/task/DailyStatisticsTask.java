package com.gem.loganalysis.task;

import com.gem.loganalysis.model.entity.DailyData;
import com.gem.loganalysis.model.vo.VulnDataVO;
import com.gem.loganalysis.service.IDailyDataService;
import com.gem.loganalysis.service.VulnerabilityService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@EnableScheduling
@Component
@Slf4j
public class DailyStatisticsTask {

    @Resource
    private IDailyDataService dataService;
    @Resource
    private VulnerabilityService vulnerabilityService;

    private static final int MAX_RETRIES = 3; // 最大重试次数
    private int retryCount = 0; // 当前重试次数

    @Scheduled(cron = "59 22 15 * * *") // 每天的23:19:59执行
    public void saveDailyStatistics() {
        try {
            boolean success = saveStatisticsData();
            if (!success) {
                handleTaskFailure();
            }else {
                // 存储成功，重置重试计数
                retryCount = 0;
            }
        } catch (Exception e) {
            handleTaskFailure();
        }
    }

    private boolean saveStatisticsData() throws JSONException {
        // 从风险的接口中取数值
        //TODO 资产评分和设备出口流量
        VulnDataVO vulnDataVO = vulnerabilityService.getAggregateForVulnBySeverity();
        DailyData dailyData = new DailyData();
        dailyData.setDateTime(vulnDataVO.getDate());
        dailyData.setLowRiskCount(vulnDataVO.getLow());
        dailyData.setMediumRiskCount(vulnDataVO.getMiddle());
        dailyData.setHighRiskCount(vulnDataVO.getHigh());
        dailyData.setExportDeviceLoad(900.23); //设备进出口值
        dailyData.setSecurityDeviceAssetScore(68.54); //安全设备评分
        dailyData.setNetworkDeviceAssetScore(74.68); //网络设备评分
        dailyData.setItDeviceAssetScore(70.54); //IT设备评分
        dailyData.setLogicalAssetScore(83.32); //逻辑资产评分
        dailyData.setTotalScore(vulnDataVO.getScore());
        return dataService.save(dailyData);
    }

    private void handleTaskFailure() {
        System.out.println("fail");
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            // 等待一段时间后进行重试
            try {
                //Thread.sleep(600000); // 10分钟后重试
                Thread.sleep(5000); // 10分钟后重试
            } catch (InterruptedException e) {
                // 处理中断异常
                Thread.currentThread().interrupt();
            }
            saveDailyStatistics(); // 重试执行任务
        } else {
            // 达到最大重试次数，打印日志
            log.warn("Failed to save daily statistics data");
            // 重置重试计数
            retryCount = 0;
        }
    }

}
