package com.gem.loganalysis.snmp;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 胡继盟
 */
@Slf4j
public class SnmpUtil {

    /**
     * 版本
     */
    public static final int DEFAULT_VERSION = SnmpConstants.version2c;

    /**
     * 传输协议
     */
    public static final String DEFAULT_PROTOCOL = "udp";

    /**
     * 传输端口
     */
    public static final int DEFAULT_PORT = 161;

    /**
     * 时延
     */
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;

    /**
     * 重连次数
     */
    public static final int DEFAULT_RETRY = 3;


    public static void main(String[] args) {
        SnmpUtil snmpUtil = new SnmpUtil();
        List<MemoryAndDiskInfo> list = snmpUtil.calculateMemoryAndDiskSeizureRate("123.60.214.137", "SZ-SNMP-PRIVATE");
        for (MemoryAndDiskInfo diskInfo : list) {
            log.info(diskInfo.toString());
        }

//        snmpWalk("123.60.214.137", "SZ-SNMP-PRIVATE", ".1.3.6.1.2.1.25.3.3.1.2");
//        snmpGet("123.60.214.137", "SZ-SNMP-PRIVATE", ".1.3.6.1.4.1.2021.11.9.0");


        ArrayList<String> oidList = new ArrayList<>();
        // 用户CPU百分比
        oidList.add(".1.3.6.1.4.1.2021.11.9.0");
        // 系统CPU百分比
        oidList.add(".1.3.6.1.4.1.2021.11.10.0");
        // 空闲CPU占比
        oidList.add(".1.3.6.1.4.1.2021.11.11.0");
        // 虚拟内存
        oidList.add(".1.3.6.1.4.1.2021.4.3.0");
        // 可用空间
        oidList.add(".1.3.6.1.4.1.2021.4.4.0");
        // 总空间
        oidList.add(".1.3.6.1.4.1.2021.4.5.0");
        // 已用内存
        oidList.add(".1.3.6.1.4.1.2021.4.6.0");
        // 空闲内存
        oidList.add(".1.3.6.1.4.1.2021.4.11.0");

//        snmpGetList("123.60.214.137", "SZ-SNMP-PRIVATE", oidList);
    }

    /**
     * 创建对象communityTarget，用于返回target
     *
     * @param ip        目标地址
     * @param community 目标团体名
     * @return CommunityTarget 设定远程实体
     */
    public static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT);
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * 根据OID获取单条消息
     *
     * @param ip        目标设备IP
     * @param community 社区号
     * @param oid       OID
     * @return 设备信息
     */
    public static String snmpGet(String ip, String community, String oid) {
        CommunityTarget target = createDefault(ip, community);
        Address address = target.getAddress();
        if (address == null) {
            log.warn("{} 转换后的Address为NULL", ip);
        }

        // 实例化一个snmp对象,使用UDP协议作为SNMP的传输层协议
        try {
            Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
            PDU pdu = new PDU();

            // pdu.add(new VariableBinding(new OID(new int[]{1,3,6,1,2,1,1,2})));
            pdu.add(new VariableBinding(new OID(oid)));

            // 接口对象的listen方法，让程序监听snmp消息
            snmp.listen();
            // 设置pdu获取数据类型
            pdu.setType(PDU.GET);
            // 调用 ResponseEvent.send(PDU pdu, Target target)发送pdu，该方法返回一个ResponseEvent对象
            ResponseEvent respEvent = snmp.send(pdu, target);
            // 输出响应发送方的传输地址
            // 通过ResponseEvent对象来获得SNMP请求的应答pdu
            PDU response = respEvent.getResponse();

            if (response == null) {
                log.warn("response is null, request time out");
            } else {
                // 获取pdu的变量绑定数
                log.info("response pdu size is {}", response.size());
                // 通过应答pdu获得mib信息（之前绑定的OID的值），方法：VariableBinding.get（int index）
                VariableBinding vb = response.get(0);
                log.info("OID: {}, VALUE: {}", vb.getOid(), vb.getVariable());
                return vb.getVariable().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据OID列表，一次获取多条OID数据，并且以List形式返回
     *
     * @param ip        目标设备IP地址
     * @param community 社区号
     * @param oidList   OID列表
     * @return 获取到的设备OID及其信息
     */
    public static List<String> snmpGetList(String ip, String community, List<String> oidList) {
        List<String> result = new ArrayList<>();
        CommunityTarget target = createDefault(ip, community);
        // 实例化一个snmp对象,使用UDP协议作为SNMP的传输层协议
        try {
            Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(String.valueOf(oid))));
            }
            // 接口对象的listen方法，让程序监听snmp消息
            snmp.listen();
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            PDU response = respEvent.getResponse();
            if (response == null) {
                log.warn("response is null, request time out");
            } else {
                log.info("response pdu size is {}", response.size());
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                    result.add(vb.getOid() + " = " + vb.getVariable());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*根据OID列表，采用异步方式一次获取多条OID数据，并且以List形式返回*/
    /*　当使用异步模式的时候，我们需要实例化一个实现了ResponseListener接口的对象，作为响应消息的监听对象。*/
    public static void snmpAsynGetList(String ip, String community, List<String> oidList) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();

            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + respEvent.getPeerAddress());
            PDU response = respEvent.getResponse();

            /*异步获取*/
            /*重写ResponseListener的OnResponse函数，该函数是一个回调函数，用来处理程序收到响应后的一些操作。*/
            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    PDU request = event.getRequest();
                    System.out.println("[request]:" + request);
                    if (response == null) {
                        System.out.println("[ERROR]: response is null");
                    } else if (response.getErrorStatus() != 0) {
                        System.out.println("[ERROR]: response status" + response.getErrorStatus() + " Text:" + response.getErrorStatusText());
                    } else {
                        System.out.println("Received response Success!");
                        for (int i = 0; i < response.size(); i++) {
                            VariableBinding vb = response.get(i);
                            System.out.println(vb.getOid() + " = " + vb.getVariable());
                        }
                        System.out.println("SNMP Asyn GetList OID finished. ");
                        latch.countDown();
                    }
                }
            };

            pdu.setType(PDU.GET);
            snmp.send(pdu, target, null, listener);
            System.out.println("asyn send pdu wait for response...");

            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);

            snmp.close();

            System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
    }

    /**
     * 根据targetOID，获取树形数据
     *
     * @param ip        目标设备IP
     * @param community 密码
     * @param targetOid 要遍历的根OID
     * @return 信息
     */
    public static List<VariableBinding> snmpWalk(String ip, String community, String targetOid) {
        List<VariableBinding> valueList = new ArrayList<>();
        CommunityTarget target = createDefault(ip, community);
        try {
            Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
            OID targetOID = new OID(targetOid);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(targetOID));

            boolean finished = false;

            while (!finished) {
                VariableBinding vb;
                ResponseEvent respEvent = snmp.getNext(pdu, target);
                PDU response = respEvent.getResponse();
                if (null == response) {
                    log.warn("responsePDU == null");
                    break;
                } else {
                    vb = response.get(0);
                }
                // check finish
                finished = checkWalkFinished(targetOID, pdu, vb);
                if (!finished) {
                    log.info(vb.toString());
                    valueList.add(vb);
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                } else {
                    log.info("SNMP walk OID has finished.");
                    snmp.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueList;
    }

    private static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            System.out.println("[true] responsePDU.getErrorStatus() != 0 ");
            System.out.println(pdu.getErrorStatusText());
            finished = true;
        } else if (vb.getOid() == null) {
            System.out.println("[true] vb.getOid() == null");
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            System.out.println("[true] vb.getOid().size() < targetOID.size()");
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            System.out.println("[true] targetOID.leftMostCompare() != 0");
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            System.out.println("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            System.out.println("[true] Variable received is not " + "lexicographic successor of requested " + "one:");
            System.out.println(vb + " <= " + targetOID);
            finished = true;
        }
        return finished;

    }

    /*根据targetOID，异步获取树形数据*/
    public static void snmpAsynWalk(String ip, String community, String oid) {
        final CommunityTarget target = createDefault(ip, community);
        Snmp snmp;
        try {
            System.out.println("----> demo start <----");

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            final PDU pdu = new PDU();
            final OID targetOID = new OID(oid);
            final CountDownLatch latch = new CountDownLatch(1);
            pdu.add(new VariableBinding(targetOID));

            ResponseListener listener = new ResponseListener() {
                @Override
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);

                    try {
                        PDU response = event.getResponse();
                        // PDU request = event.getRequest();
                        // System.out.println("[request]:" + request);
                        if (response == null) {
                            System.out.println("[ERROR]: response is null");
                        } else if (response.getErrorStatus() != 0) {
                            System.out.println("[ERROR]: response status" + response.getErrorStatus() + " Text:" + response.getErrorStatusText());
                        } else {
                            System.out.println("Received Walk response value :");
                            VariableBinding vb = response.get(0);

                            boolean finished = checkWalkFinished(targetOID, pdu, vb);
                            if (!finished) {
                                System.out.println(vb.getOid() + " = " + vb.getVariable());
                                pdu.setRequestID(new Integer32(0));
                                pdu.set(0, vb);
                                ((Snmp) event.getSource()).getNext(pdu, target, null, this);
                            } else {
                                System.out.println("SNMP Asyn walk OID value success !");
                                latch.countDown();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }

                }
            };

            snmp.getNext(pdu, target, null, listener);
            System.out.println("pdu 已发送,等到异步处理结果...");

            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);
            snmp.close();

            System.out.println("----> demo end <----");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Asyn Walk Exception:" + e);
        }
    }

    /**
     * 根据OID和指定string来设置设备的数据
     *
     * @param ip        目标设备IP
     * @param community 密码
     * @param oid       OID
     * @param val       目标值
     * @throws IOException IO异常
     */
    public static void setPDU(String ip, String community, String oid, String val) throws IOException {
        CommunityTarget target = createDefault(ip, community);
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid), new OctetString(val)));
        pdu.setType(PDU.SET);

        DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        snmp.listen();
        snmp.send(pdu, target);
        snmp.close();
    }

    /**
     * 根据IP和社区密码,计算目标设备的内存和磁盘使用情况
     *
     * @param ip        目标设备IP
     * @param community 社区密码
     * @return 内存和磁盘使用情况
     */
    public List<MemoryAndDiskInfo> calculateMemoryAndDiskSeizureRate(String ip, String community) {
        List<VariableBinding> list = snmpWalk(ip, community, ".1.3.6.1.2.1.25.2.3.1");
        HashMap<String, MemoryAndDiskInfo> map = new HashMap<>();
        for (VariableBinding binding : list) {
            String oidStr = binding.getOid().toString();
            String oidValue = binding.getVariable().toString();
            if (oidStr.startsWith("1.3.6.1.2.1.25.2.3.1.1")) {
                map.put(oidValue, new MemoryAndDiskInfo(oidValue));
                continue;
            }
            String key = oidStr.substring(oidStr.lastIndexOf(".") + 1);
            if (oidStr.startsWith("1.3.6.1.2.1.25.2.3.1.3")) {
                map.get(key).setDeviceType(oidValue);
                continue;
            }
            if (oidStr.startsWith("1.3.6.1.2.1.25.2.3.1.5")) {
                map.get(key).setTotalSize(Integer.parseInt(oidValue));
                continue;
            }
            if (oidStr.startsWith("1.3.6.1.2.1.25.2.3.1.6")) {
                map.get(key).setUsedSize(Integer.parseInt(oidValue));
            }
        }
        return new ArrayList<>(map.values());
    }
}

