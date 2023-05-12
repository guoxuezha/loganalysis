package com.gem.loganalysis.scanner;

import com.gem.loganalysis.service.ILogicalAssetTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * 端口、指纹扫描器
 * 职责：
 * 1、解析输入的ip地址、端口
 * 2、新键扫描任务ScanJob扫描引擎进行端口扫描
 * 3、两种扫描方式：TCP全连接扫描、TCP半连接扫描
 *
 */
public class Scanner {
    public static final int cpuCores = Runtime.getRuntime().availableProcessors();
    // 日志
    private static Logger logger = Logger.getLogger("Scanner");
    // 使用多线程扫描
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor((cpuCores * 2 + 1) * 10
            ,(cpuCores * 2 + 1) * 100,1000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());


    /**
     * 开始方法
     * @param ips 输入的待扫描ip列表
     * @param ports 输入的待扫描端口
     */
    @Async
    public static void start(String ips, String ports) {
        // 解析端口
        String[] portArray = ports.split("-");
        int portStart = Integer.parseInt(portArray[0]);
        int portEnd = Integer.parseInt(portArray[1]);
        logger.info("[-] Ports: " + ports);
        // 解析ip地址，并扫描
        if (ips.indexOf(',') != -1) {
            String[] ipList = ips.split(",");
            logger.info("[-] IP list: " + ipList.toString());
            for (String ip : ipList) {
                scanAllPort(ip,portStart,portEnd);
            }
        }else if (ips.indexOf('/') != -1){
            // TODO 支持ip地址网段的解析
            String[] ipArray = ips.split("/");
            String ipAddress = ipArray[0];
            int mask = Integer.parseInt(ipArray[1]);
            String[] ipSplit = ipAddress.split(".");

        }else {
            scanAllPort(ips,portStart,portEnd);
        }
        try{
            while(true){
                if(poolExecutor.getActiveCount() == 0){
                    logger.info("[-] Scan job all finish");
                    break;
                }
                Thread.sleep(1000);
            }
        }catch (Exception ex){
            logger.warning("End with exeception, ex: " + ex.getMessage());
        }
    }

    /**
     * 扫描某ip的对应端口
     * @param ip ip地址
     * @param portStart 开始扫描的的端口
     * @param portEnd 停止扫描的端口
     */
    public static void scanAllPort(String ip, int portStart, int portEnd){
        for (int port = portStart; port <= portEnd; port++){
            scan(ip,port);
        }
    }

    /**
     * 对ip:port进行扫描
     * @param ip ip地址
     * @param port 端口
     */
    public static void scan(String ip, int port){

        // 执行扫描任务
        poolExecutor.execute(new ScanJob(new ScanObject(ip,port),ScanEngine.TCP_FULL_CONNECT_SCAN));

    }
}
