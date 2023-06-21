package com.gem.loganalysis.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gem.loganalysis.enmu.AssetClass;
import com.gem.loganalysis.enmu.ScannerType;
import com.gem.loganalysis.mapper.LogicalAssetTempMapper;
import com.gem.loganalysis.mapper.PhysicalAssetTempMapper;
import com.gem.loganalysis.model.dto.asset.VlanDTO;
import com.gem.loganalysis.model.entity.Asset;
import com.gem.loganalysis.model.entity.OrgVlan;
import com.gem.loganalysis.scanner.IpScanner;
import com.gem.loganalysis.scanner.Scanner;
import com.gem.loganalysis.service.*;
import com.gem.loganalysis.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@EnableScheduling
@Component
@Slf4j
public class DailyScanTask {

    @Resource
    private IAssetService assetService;
    @Resource
    private IOrgVlanService orgVlanService;
    @Resource
    private LogicalAssetTempMapper logicalAssetTempMapper;
    @Resource
    private PhysicalAssetTempMapper physicalAssetTempMapper;



    //对配置的网段进行IP+逻辑资产的扫描  对现有的资产进行全扫描
    @Scheduled(cron = "00 00 02 * * *") // 每天的02:00:00执行
    public void saveDailyStatistics() {
        try {
            //清空逻辑资产表
            logicalAssetTempMapper.cleanTable();
            //清空物理资产表
            physicalAssetTempMapper.cleanTable();
            Thread.sleep(5000);
            //全部区段扫描
            List<OrgVlan> list = orgVlanService.list();
            list.forEach(e->{
                if(StringUtils.isNotBlank(e.getVlan().trim())){
                    List<VlanDTO> vlanDTOS = JsonUtils.parseArray(e.getVlan(), VlanDTO.class);
                    if(vlanDTOS.size()>0){
                        IpScanner.scannerIpSection(vlanDTOS
                                , DateUtil.format(new Date(),"yyyyMMddHHmmss")
                                ,"系统"
                                ,"SIMPLE"
                                , ScannerType.AUTOMATIC.getId());
                    }
                }
            });
            //五分钟后开始扫描已有端口
            Thread.sleep(300000);
            //扫描全部IP端口
            List<Asset> asset = assetService.list(new LambdaQueryWrapper<Asset>()
                    .eq(Asset::getAssetClass, AssetClass.PHYSICAL.getId()));

            asset.forEach(e->{
                Scanner.start(e.getIpAddress(),"1-65535",
                        DateUtil.format(new Date(),"yyyyMMddHHmmss")
                        ,"系统"
                        ,ScannerType.AUTOMATIC.getId());
                try {
                    Thread.sleep(60000); //每隔一分钟开启一次
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            });
        } catch (Exception e) {
            log.info(" daily scan error"+e.getMessage());
        }
    }


}
