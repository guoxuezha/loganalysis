package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
import com.gem.loganalysis.util.GetBeanUtil;

import java.util.Date;

/**
 * Ip区段扫描任务类，执行具体的扫描任务
 */
public class IpScanJob implements Runnable {
    // 扫描信息
    private String ip;

    private String scanTime;

    private IPhysicalAssetTempService physicalAssetTempService;

    public IpScanJob(String ip,String scanTime) {
        this.ip = ip;
        this.scanTime = scanTime;
    }

    @Override
    public void run() {
        IpScanObject scan = IpScanner.pingDeviceIp(ip);
        if(scan!=null){
            this.physicalAssetTempService = GetBeanUtil.getApplicationContext().getBean(IPhysicalAssetTempService.class);
            boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus(scan.getIsOpen()?"1":"0")
                    .setAssetOrg("测试")//TODO 目前还没有部门 之后从登录人获取部门
                    .setIpAddress(scan.getIp())
                    .setScanTime(scanTime));
        }
    }
}
