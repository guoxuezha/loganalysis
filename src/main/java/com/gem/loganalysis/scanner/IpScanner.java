package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.dto.IpSectionDTO;
import com.gem.loganalysis.model.dto.asset.VlanDTO;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.model.entity.OrgVlan;
import com.gem.loganalysis.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;
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
        int timeOut = 5000;
        boolean reachable =false;
        try{
            reachable = InetAddress.getByName(ip).isReachable(timeOut);
        }catch (IOException e){
        }
        return new IpScanObject().setIsOpen(reachable).setIp(ip);
    }

    /**
     * IP区段扫描
     */
    public static void scannerIpSection(OrgVlan orgVlan, String scanTime){
        List<VlanDTO> vlanDTOS = JsonUtils.parseArray(orgVlan.getVlan(), VlanDTO.class);
        vlanDTOS.forEach(e->{
            String ipBefore = StringUtils.substringBeforeLast(e.getBeginIp(), ".");
            String begin = StringUtils.substringAfterLast(e.getBeginIp(), ".");
            String end = StringUtils.substringAfterLast(e.getEndIp(), ".");
            //把网段拼接成IP开启线程
            for(int i = Integer.parseInt(begin);i <= Integer.parseInt(end);i++){
                String ip = ipBefore+'.'+i;
                // 执行扫描任务
                poolExecutor.execute(new IpScanJob(ip,scanTime));
                //poolExecutor.execute(new ScanJob(new ScanObject(ip,0),ScanEngine.TCP_FULL_CONNECT_SCAN,scanTime));
            }
        });
    }

}
