package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
import com.gem.loganalysis.util.GetBeanUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ip区段扫描任务类，执行具体的扫描任务
 */
public class IpScanJob implements Runnable {
    // 扫描信息
    private String ip;

    private String scanTime;

    private String orgId;

    private IPhysicalAssetTempService physicalAssetTempService;

    public IpScanJob(String ip,String scanTime,String orgId) {
        this.ip = ip;
        this.scanTime = scanTime;
        this.orgId = orgId;
    }

    @Override
    public void run() {
        IpScanObject scan = IpScanner.pingDeviceIp(ip);
        this.physicalAssetTempService = GetBeanUtil.getApplicationContext().getBean(IPhysicalAssetTempService.class);
        if(scan!=null&& scan.getIsOpen()){
            boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("1")
                    .setAssetOrg(orgId!=null?orgId:"")
                    .setIpAddress(ip)
                    .setScanTime(scanTime));
            //System.out.println("ping扫描出了IP"+ip);
        }else {
            //ping未发现IP，继续TCP扫描
            AtomicBoolean foundResult = new AtomicBoolean(false); // 使用 AtomicBoolean 替代 boolean

            for (int i = 1; i <= 1000; i++) {
                int port = i;
                Thread thread = new Thread(() -> {
                    ScanObject scanObject = ScanEngine.scan(new ScanObject(ip, port), ScanEngine.TCP_FULL_CONNECT_SCAN);
                    if (scanObject.getOpen() != null && scanObject.getOpen()) {
                        //System.out.println("TCP扫描出了" + ip);
                        boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("1")
                                .setAssetOrg(orgId != null ? orgId : "")
                                .setIpAddress(ip)
                                .setScanTime(scanTime));
                        foundResult.set(true); // 修改 AtomicBoolean 的值
                    }
                });
                thread.start();

                if (foundResult.get()) { // 在循环外部访问 AtomicBoolean 的值
                    break; // 停止循环
                }
            }

            if (!foundResult.get()) { // 在循环外部访问 AtomicBoolean 的值
                //System.out.println("没有结果" + ip);
                boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("0")
                        .setAssetOrg(orgId != null ? orgId : "")
                        .setIpAddress(ip)
                        .setScanTime(scanTime));
            }

        }

          /*  boolean foundResult = false; // 标志变量，用于指示是否找到结果
            for (int i=1;i<=900;i++){
                ScanObject scanObject = ScanEngine.scan(new ScanObject(ip,i), ScanEngine.TCP_FULL_CONNECT_SCAN);
                if(scanObject.getOpen()!=null&&scanObject.getOpen()){
                    System.out.println("TCP扫描出了"+ip);;
                    boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("1")
                            .setAssetOrg(orgId!=null?orgId:"")
                            .setIpAddress(ip)
                            .setScanTime(scanTime));
                    foundResult = true;
                    break;
                }
            }
            if (!foundResult) {
                System.out.println("没有结果"+ip);
                boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("0")
                        .setAssetOrg(orgId!=null?orgId:"")
                        .setIpAddress(ip)
                        .setScanTime(scanTime));
            }
        }*/
    }
}
