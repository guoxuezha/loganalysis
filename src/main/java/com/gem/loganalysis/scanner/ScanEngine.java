package com.gem.loganalysis.scanner;

import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.entity.LogicalAssetTemp;
import com.gem.loganalysis.service.IAssetGroupService;
import com.gem.loganalysis.service.ILogicalAssetTempService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

/**
 * 扫描器引擎
 * 实现具体的扫描逻辑，提供扫描能力（TCP全连接和半连接扫描）
 */
public class ScanEngine {
    private static Logger logger = Logger.getLogger("TCPFullConnectScan");
    public static final String TCP_FULL_CONNECT_SCAN = "TCP_FULL_CONNECT_SCAN";
    public static final String TCP_HALF_CONNECT_SCAN = "TCP_HALF_CONNECT_SCAN";


        public static ScanObject scan(ScanObject object, String scanEngine){
        switch (scanEngine){
            case TCP_FULL_CONNECT_SCAN:
                return tcpFullConnectScan(object);
            case TCP_HALF_CONNECT_SCAN:
                return tcpHalfConnectScan(object);
        }
        return tcpFullConnectScan(object);
    }

    /**
     * tcp全连接扫描
     * @param object
     * @return
     */
    private static ScanObject tcpFullConnectScan(ScanObject object){
        try{
            // 全连接扫描，发现可用服务
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(object.getIp(),object.getPort()),600);
            object.setOpen(true);
            object.setService();
            // 发送招手信息
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            out.write("hello");
            out.flush();
            // 获取服务指纹
            BufferedReader re = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serviceBanner = re.readLine();
            object.setBanner(serviceBanner);
            object.setService();
            logger.info("[-] Find service :"+ object);
            socket.close();
        } catch (ConnectException e) {
            // 打印连接失败的端口
//            logger.info("[-] Close: " + object.getIp() + ":" + object.getPort());
        } catch (Exception e){
            // 出现其他异常
           // logger.info("[-] " + object.toString() + "end with unexecepted exeception:" + e.getMessage());
        }
        return object;

    }

    /**
     * TPC 半连接扫描
     * @param object
     * @return
     */
    private static ScanObject tcpHalfConnectScan(ScanObject object){
        // TODO 待实现tcp半连接扫描
        return object;
    }
}
