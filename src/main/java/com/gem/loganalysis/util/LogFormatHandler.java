package com.gem.loganalysis.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.gem.loganalysis.model.bo.RsyslogNormalMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Description:
 * Date: 2023/4/23 18:30
 *
 * @author GuoChao
 **/
public class LogFormatHandler {

    /*public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg1 = "Apr 23 00:00:36 172.16.200.74 rule_id: 1;time:2023-04-23 00:18:00;module:fw;src_intf:T2/1;dst_intf:;action:accept;proto:tcp;src_addr:61.147.19.51;src_port:21234;dst_addr:222.95.84.100;dst_port:443;src_addr_nat:;src_port_nat:;dst_addr_nat:;dst_port_nat:;info:;user:";
        System.out.println(objectMapper.writeValueAsString(Probe.getInstance().parseMsgToJson(msg1)));

        String msg2 = "Apr 23 00:32:04 172.16.200.77  device_type=ips;manufacturers=nsfocus;security_name=ips_log;time=1682180940;card=T1/4;sip=110.249.201.96;smac=C4:0D:96:39:47:EE;sport=57732;dip=222.95.84.20;dmac=88:DF:9E:2D:56:01;dport=80;vid=0;ruleid=50586;event=网络爬虫头条抓取网页信息;module=0;threat_level=1;threat_type=045 事件监控;attack_type=4;action=1;acted=1;count=1;protocol=TCP;user_name=;smt_user=;policy_id=1;digest=SFRUUG5TZjBDdXNDTElFTlQ=;direction=client;szonename=Monitor;dzonename=;rawinfo=iN+eLVYBxA2WOUfuCABFAAIM6HtAADUG8KJu+clg3l9UFOGEAFCWPtUULMOmyoAYAB2X/wAAAQEICr9cZEPdmFLWR0VUIC9odG1sL25ld3MvZGV0YWlsXzIwMTVfMDgvMDMvNDExNzYuc2h0bWwgSFRUUC8xLjENCmhvc3Q6IHhuLS13cXZxN2lxemF6MzJjbG5hMjZvaTd1LmNvbQ0KdXBncmFkZS1pbnNlY3VyZS1yZXF1ZXN0czogMQ0KdXNlci1hZ2VudDogTW96aWxsYS81LjAgKExpbnV4OyBBbmRyb2lkIDUuMCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgTW9iaWxlIFNhZmFyaS81MzcuMzYgKGNvbXBhdGlibGU7IEJ5dGVzcGlkZXI7IGh0dHBzOi8vemhhbnpoYW5nLnRvdXRpYW8uY29tLykNCmFjY2VwdC1sYW5ndWFnZTogemgsemgtQ047cT0wLjkNCmFjY2VwdDogdGV4dC9odG1sLGFwcGxpY2F0aW9uL3hodG1sK3htbCxhcHBsaWNhdGlvbi94bWw7cT0wLjksaW1hZ2Uvd2VicCxpbWFnZS9hcG5nLCovKjtxPTAuOA0KYWNjZXB0LWVuY29kaW5nOiBnemlwLCBkZWZsYXRlDQpDb25uZWN0aW9uOiBLZWVwLUFsaXZlDQoNCg==;rawlen=720;cdnip=;extension=;popular=1;affect_os=Windows,Linux/Unix;service=MISC;ar=2;cve_id=;cwe_id=;cnnvd_id=;src_asset=0;dst_asset=0;scountry=中国;scity=石家庄市;dcountry=中国;dcity=南京市";
        System.out.println(objectMapper.writeValueAsString(PoisonProofWall.getInstance().parseMsgToJson(msg2)));

        String msg3 = "Apr  8 00:00:36 222.95.84.169 {\"asset_name\": \"高敏沙箱\",\"dev_name\":\"80b2dd04-b5ce-802a-9a41-66229557be59\",\"vm_ip\":\"\",\"attack_ip\":\"100.100.2.136\",\"attack_ip_info\":{\"code\":\"*\",\"city\":\"共享地址\",\"country\":\"共享地址\",\"latitude\":\"\",\"longitude\":\"\"},\"moresec_id\":\"100.100.2.136\",\"sns_data\":\"\",\"attack_type\":\"PROBE\",\"attack_level\":3,\"attack_opt_type\":521,\"attack_desc\":\"攻击者在 60s 内建立了 1 条恶意 udp 连接, 探测了 44627 端口\",\"attack_method_desc\":\"端口扫描\",\"attack_time\":\"2023-04-07T23:59:18.07946017+08:00\",\"detail\":{\"context\":{\"dip\":\"172.16.189.118\",\"dport_list\":[\"44627\"],\"end_time\":\"2023-04-08T00:00:28.077315381+08:00\",\"freq\":1,\"ip\":\"\",\"proto\":\"udp\",\"proxy_client_id\":\"0a07c85a-90bb-7ea7-6abb-e3e35239efa0\",\"role\":\"disguise\",\"sip\":\"100.100.2.136\",\"start_time\":\"2023-04-07T23:59:18.07946017+08:00\",\"time\":\"0001-01-01 00:00:00\"},\"insert_tm\":\"2023-04-07T23:59:18.07946017+08:00\",\"proxy_client_id\":\"0a07c85a-90bb-7ea7-6abb-e3e35239efa0\",\"proxy_extra\":\"disguise\",\"proxy_ip\":\"172.16.189.118\",\"proxy_type\":2,\"src_ip\":\"100.100.2.136\"},\"sandbox_type\":\"\",\"attack_asset_ip\":\"172.16.189.118\",\"attack_asset_port\":0,\"attack_asset_role\":\"disguise\",\"attack_asset_tag\":\"测试客户\"}";
        System.out.println(objectMapper.writeValueAsString(Honeypot.getInstance().parseMsgToJson(msg3)));
    }*/

    /**
     * 解析日志内容,转为MAP对象
     *
     * @param log       日志原文对象
     * @param keyList   关注KEY列表
     * @param itemSplit 数据项切分字符
     * @param kvSplit   键值切分字符
     * @return Map对象
     */
    public static HashMap<String, Object> parseMsgToJson(RsyslogNormalMessage log, List<String> keyList, String itemSplit, String kvSplit) {
        return getMessageInfoMap(log.getMessage(), itemSplit, kvSplit);
    }

    /**
     * 从日志头中提取信息
     *
     * @param msg 日志消息
     * @return 头信息
     */
    public static String[] getHeaderInfo(String msg) {
        while (msg.startsWith(" ")) {
            msg = msg.substring(1);
        }
        String[] result = new String[3];
        DateTime parse = DateUtil.parse(msg.substring(0, 15), "MMM dd HH:mm:ss", Locale.US);
        result[0] = parse.toString().replaceAll("1970", String.valueOf(DateUtil.thisYear()));
        int i = msg.substring(16).indexOf(" ") + 16;
        result[1] = msg.substring(16, i);
        result[2] = String.valueOf(i + 1);
        return result;
    }

    /**
     * 将日志消息体解析为JSON对象
     *
     * @param msg       日志消息体
     * @param itemSplit 属性分隔符
     * @param kvSplit   键值分隔符
     * @return 日志内容对象
     */
    public static HashMap<String, Object> getMessageInfoMap(String msg, String itemSplit, String kvSplit) {
        HashMap<String, Object> map = new HashMap<>();
        for (String kv : msg.trim().split(itemSplit)) {
            String[] split = kv.split(kvSplit);
            if (split.length == 2) {
                map.put(split[0], split[1]);
            } else if (split.length == 1) {
                map.put(split[0], null);
            } else {
                map.put(split[0], kv.substring(split[0].length()));
            }
        }
        return map;
    }

}
