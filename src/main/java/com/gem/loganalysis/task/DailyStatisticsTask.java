package com.gem.loganalysis.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gem.loganalysis.enmu.AssetClass;
import com.gem.loganalysis.model.dto.asset.AssetQueryDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.DailyData;
import com.gem.loganalysis.model.vo.VulnDataVO;
import com.gem.loganalysis.model.vo.asset.AssetRespVO;
import com.gem.loganalysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@EnableScheduling
@Component
@Slf4j
public class DailyStatisticsTask {

    @Resource
    private IDailyDataService dataService;
    @Resource
    private VulnerabilityService vulnerabilityService;
    @Resource
    private IAssetService assetService;
    @Resource
    private IPhysicalAssetTempService physicalAssetTempService;
    @Resource
    private ILogicalAssetTempService logicalAssetTempService;

    private static final int MAX_RETRIES = 3; // 最大重试次数
    private int retryCount = 0; // 当前重试次数

    @Scheduled(cron = "59 19 23 * * *") // 每天的23:19:59执行
    public void saveDailyStatistics() {
        try {
            boolean success = saveStatisticsData();
            if (!success) {
                handleTaskFailure();
            }else {
                // 存储成功，重置重试计数
                retryCount = 0;
                log.info("save daily statistics data success");
            }
        } catch (Exception e) {
            handleTaskFailure();
        }
    }

    private boolean saveStatisticsData() throws JSONException {
        // 从风险的接口中取数值
        VulnDataVO vulnDataVO = vulnerabilityService.getAggregateForVulnBySeverity();
        //物理资产扫描未纳管数量
        Integer unmanagedPhysicalCount = physicalAssetTempService.getUnmanagedCount();
        //逻辑资产扫描未纳管数量
        Integer unmanagedLogicalCount = logicalAssetTempService.getUnmanagedCount();
        //各类资产评分
        //先查物理资产
        List<AssetRespVO> assetList = assetService.getAssetList(new AssetQueryDTO().setAssetClass(AssetClass.PHYSICAL.getId()));
        double SecurityAverageScore = assetList.stream()
                .filter(asset -> "安全设备".equals(asset.getAssetCategory()))
                .mapToDouble(AssetRespVO::getScore)
                .average()
                .orElse(0.0);
        double netWorkAverageScore = assetList.stream()
                .filter(asset -> "网络设备".equals(asset.getAssetCategory()))
                .mapToDouble(AssetRespVO::getScore)
                .average()
                .orElse(0.0);
        double itAverageScore = assetList.stream()
                .filter(asset -> "服务器".equals(asset.getAssetCategory())||"存储设备".equals(asset.getAssetCategory()))
                .mapToDouble(AssetRespVO::getScore)
                .average()
                .orElse(0.0);

        //物理资产在线数量
        long physicalCount = assetService.count(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getAssetClass, AssetClass.PHYSICAL.getId())
                .eq(Asset::getAssetStatus, "0"));
        //逻辑资产在线数量
        long logicalCount = assetService.count(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getAssetClass, AssetClass.LOGICAL.getId())
                .eq(Asset::getAssetStatus, "0"));
        //逻辑资产暂时不评分
        DailyData dailyData = new DailyData();
        dailyData.setDateTime(vulnDataVO.getDate());
        dailyData.setLowRiskCount(vulnDataVO.getLow());
        dailyData.setMediumRiskCount(vulnDataVO.getMiddle());
        dailyData.setHighRiskCount(vulnDataVO.getHigh());
        dailyData.setExportDeviceLoad(900.00); //这个值没用了 可删
        dailyData.setSecurityDeviceAssetScore(SecurityAverageScore); //安全设备评分
        dailyData.setNetworkDeviceAssetScore(netWorkAverageScore); //网络设备评分
        dailyData.setItDeviceAssetScore(itAverageScore); //IT设备评分
        dailyData.setLogicalAssetScore(100.00); //逻辑资产评分
        dailyData.setLogicalAssetsOnlineCount((int)logicalCount);//逻辑资产在线数量
        dailyData.setPhysicalAssetsOnlineCount((int)physicalCount);//物理资产在线数量
        dailyData.setPhysicalAssetsScanCount(unmanagedPhysicalCount);//物理资产扫描数量
        dailyData.setLogicalAssetsScanCount(unmanagedLogicalCount);//逻辑资产扫描数量
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
