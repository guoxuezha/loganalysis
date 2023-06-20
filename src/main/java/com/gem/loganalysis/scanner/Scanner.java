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

    private static int[] commonPorts = {20, 21, 22, 23, 25, 53, 67, 68, 80, 110, 139, 143, 161, 389, 443, 445, 512, 513, 514, 873, 1080, 1352, 1433, 1521, 2049, 2181, 2375, 3306, 3389, 4848, 5000, 5432, 5632, 5900, 6379, 7001, 7002, 8069, 8161, 8080, 8081, 8082, 8083, 8084, 8085, 8086, 8087, 8088, 8089, 8090, 8443, 8888, 9000, 9090, 9200, 9300, 11211, 27017, 27018};




    // 使用多线程扫描
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20*cpuCores
            ,40*cpuCores,1000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());


    /**
     * 开始方法
     * @param ips 输入的待扫描ip列表
     * @param ports 输入的待扫描端口
     */
    public static void start(String ips, String ports,String scanTime,String orgId,Integer type) {
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
                scanAllPort(ip,portStart,portEnd,scanTime,orgId,type);
            }
        }else if (ips.indexOf('/') != -1){
            String[] ipArray = ips.split("/");
            String ipAddress = ipArray[0];
            int mask = Integer.parseInt(ipArray[1]);
            String[] ipSplit = ipAddress.split(".");

        }else {
            scanAllPort(ips,portStart,portEnd,scanTime,orgId,type);
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
     * 开始方法
     * @param ips 输入的待扫描ip列表
     */
    public static void startCommon(String ips,String scanTime,String orgId,Integer type) {
        scanCommonPort(ips,scanTime,orgId,type);
        try{
            while(true){
                if(poolExecutor.getActiveCount() == 0){
                    logger.info(ips+" Common Scan job all finish");
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
    public static void scanAllPort(String ip, int portStart, int portEnd,String scanTime,String orgId,Integer type)  {
        for (int port = portStart; port <= portEnd; port++){
            scan(ip,port,scanTime,orgId,type);
            if(poolExecutor.getQueue().size()>2000){
                try {
                    Thread.sleep(5*1000);//防止阻塞队列过长内存溢出，限制开启的速率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(ip+" All Scan job finish");
    }


    /**
     * 快速扫描，扫描常用端口
     * @param ip ip地址
     */
    public static void scanCommonPort(String ip,String scanTime,String orgId,Integer type)  {
        for (int commonPort : commonPorts) {
            scan(ip,commonPort,scanTime,orgId,type);
            if(poolExecutor.getQueue().size()>2000){
                try {
                    Thread.sleep(5*1000);//防止阻塞队列过长内存溢出，限制开启的速率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(ip+" Common Scan job finish");
    }

    /**
     * 对ip:port进行扫描
     * @param ip ip地址
     * @param port 端口
     */
    public static void scan(String ip, int port,String scanTime,String orgId,Integer type){
        // 执行扫描任务
        poolExecutor.execute(new ScanJob(new ScanObject(ip,port),ScanEngine.TCP_FULL_CONNECT_SCAN,scanTime,orgId,type));
    }


}
