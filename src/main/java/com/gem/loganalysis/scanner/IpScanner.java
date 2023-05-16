package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class IpScanner {

    public static final int cpuCores = Runtime.getRuntime().availableProcessors();
    // 日志
    private static Logger logger = Logger.getLogger("Scanner");
    // 使用多线程扫描
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor((cpuCores * 2 + 1) * 10
            ,(cpuCores * 2 + 1) * 100,1000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

    /**
     * PING IP网络
     * @param  ip
     * @return boolean
     */
    public static IpScanObject pingDeviceIp(String ip){
        if(StringUtils.isEmpty(ip)){
            return null;
        }
        int timeOut = 3000;
        boolean reachable =false;
        try{
            reachable = InetAddress.getByName(ip).isReachable(timeOut);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new IpScanObject().setIsOpen(reachable).setIp(ip);
    }

    /**
     * IP区段扫描
     * @param  ipSection
     */
    public static void scannerIpSection(String ipSection,String scanTime){
        //把网段拼接成IP开启线程
        String beforeLast = StringUtils.substringBeforeLast(ipSection, ".");
        String s = StringUtils.substringAfterLast(ipSection, ".");
        Integer before = Integer.parseInt(StringUtils.substringBeforeLast(s, "/"));
        Integer after = Integer.parseInt(StringUtils.substringAfterLast(s, "/"));
        for(int i = before;i <= after;i++){
            String ip = beforeLast+"."+i;
            // 执行扫描任务
            poolExecutor.execute(new IpScanJob(ip,scanTime));
        }

    }

}
