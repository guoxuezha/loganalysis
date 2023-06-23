package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.PhysicalAssetTemp;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import com.gem.loganalysis.service.IPhysicalAssetTempService;
import com.gem.loganalysis.util.GetBeanUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.gem.loganalysis.util.UserUtil.getLoginUserOrgId;

/**
 * Ip区段扫描任务类，执行具体的扫描任务
 */
public class IpScanJob implements Runnable {
    // 扫描信息
    private String ip;

    private String scanTime;

    private String orgId;

    //扫描类型，SIMPLE,ALL
    private String scannerType;

    //类型 0自动 1手动
    private Integer type;

    private IPhysicalAssetTempService physicalAssetTempService;

    public IpScanJob(String ip,String scanTime,String orgId,String scannerType,Integer type) {
        this.ip = ip;
        this.scanTime = scanTime;
        this.orgId = orgId;
        this.scannerType = scannerType;
        this.type = type;
    }

    @Override
    public void run() {
        IpScanObject scan = IpScanner.pingDeviceIp(ip);
        this.physicalAssetTempService = GetBeanUtil.getApplicationContext().getBean(IPhysicalAssetTempService.class);
        if(scan!=null&& scan.getIsOpen()){
            boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("1")
                    .setAssetOrg(orgId!=null?orgId:"")
                    .setIpAddress(ip)
                    .setType(type)
                    .setScanTime(scanTime));
            //System.out.println("ping扫描出了IP"+ip);
            //如果扫描除了物理资产继续扫描逻辑资产
            if("ALL".equals(scannerType)){
                Scanner.start(ip,"1-65535",scanTime,orgId,type);
            }else if("SIMPLE".equals(scannerType)){
                    Scanner.startCommon(ip,scanTime,orgId,type);
            }
        }else {
            //ping未发现IP，继续TCP扫描,只要扫到了，就开始扫逻辑资产
            AtomicBoolean foundResult = new AtomicBoolean(false); // 使用 AtomicBoolean 替代 boolean

            //TCP扫描1-1000
            for (int i = 1; i <= 999; i++) {
                int port = i;
                //不使用多线程扫描
              /*  Thread thread = new Thread(() -> {
                    ScanObject scanObject = ScanEngine.scan(new ScanObject(ip, port), ScanEngine.TCP_FULL_CONNECT_SCAN);
                    if (scanObject.getOpen() != null && scanObject.getOpen()) {
                        //System.out.println("TCP扫描出了" + ip);
                        boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("1")
                                .setAssetOrg(orgId != null ? orgId : "")
                                .setIpAddress(ip)
                                .setType(type)
                                .setScanTime(scanTime));
                        foundResult.set(true); // 修改 AtomicBoolean 的值
                        //如果扫描除了物理资产继续扫描逻辑资产
                        if("ALL".equals(scannerType)){
                            Scanner.start(ip,"1-65535",scanTime,orgId,type);
                        }else if("SIMPLE".equals(scannerType)){
                            Scanner.startCommon(ip,scanTime,orgId,type);
                        }
                    }
                });
                thread.start();*/

                ScanObject scanObject = ScanEngine.scan(new ScanObject(ip, port), ScanEngine.TCP_FULL_CONNECT_SCAN);
                if (scanObject.getOpen() != null && scanObject.getOpen()) {
                    // 扫描到开放端口
                    boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("1")
                            .setAssetOrg(orgId != null ? orgId : "")
                            .setIpAddress(ip)
                            .setType(type)
                            .setScanTime(scanTime));
                    foundResult.set(true); // 修改 AtomicBoolean 的值

                    if ("ALL".equals(scannerType)) {
                        Scanner.start(ip, "1-65535", scanTime, orgId, type);
                    } else if ("SIMPLE".equals(scannerType)) {
                        Scanner.startCommon(ip, scanTime, orgId, type);
                    }

                    // 扫描到结果后，中断循环
                    break;
                }

        /*        if (foundResult.get()) { // 在循环外部访问 AtomicBoolean 的值
                    break; // 停止循环
                }*/
            }

            if (!foundResult.get()) { // 在循环外部访问 AtomicBoolean 的值
                //System.out.println("没有结果" + ip);
                boolean save = physicalAssetTempService.save(new PhysicalAssetTemp().setAssetStatus("0")
                        .setAssetOrg(orgId != null ? orgId : "")
                        .setIpAddress(ip)
                        .setType(type)
                        .setScanTime(scanTime));
            }

        }

    }
}
