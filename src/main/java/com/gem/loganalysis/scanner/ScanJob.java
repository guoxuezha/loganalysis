package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import com.gem.loganalysis.util.GetBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 扫描任务类，执行具体的扫描任务
 */
public class ScanJob implements Runnable {
    // 扫描信息
    private ScanObject object;
    // 扫描类型
    private String scanType;
    // 扫描时间批次
    private String scanTime;
    //所属部门
    private String orgId;

    private ILogicalAssetTempService logicalAssetTempService;

    public ScanJob(ScanObject object,String scanType,String scanTime,String orgId) {
        this.object = object;
        this.scanType = scanType;
        this.scanTime = scanTime;
        this.orgId = orgId;
    }

    @Override
    public void run() {
        ScanObject scan = ScanEngine.scan(object, scanType);
        if(scan.getOpen()!=null&&scan.getOpen()){
            this.logicalAssetTempService = GetBeanUtil.getApplicationContext().getBean(ILogicalAssetTempService.class);
            boolean save = logicalAssetTempService.save(new LogicalAssetTemp().setScanTime(scanTime)
                    .setAssetOrg(orgId != null ? orgId : "")
                    .setIpAddress(object.getIp())
                    .setEnablePort(object.getPort())
                    .setAssetType(object.getService())
                    .setAssetInfo(object.getBanner()));
        }
    }
}
