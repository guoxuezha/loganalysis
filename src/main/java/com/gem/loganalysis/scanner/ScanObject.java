package com.gem.loganalysis.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * 扫描信息实体
 */
public class ScanObject {
     private String ip;
     private int port;
     private String service;
     private Boolean isOpen;
     private String banner;
     // 存放服务指纹和服务的对应关系 banner -> service
     private static Map<String,String> bannerMaps = new HashMap<>();
     // 存放常见端口与服务的对应关系 port -> service
     private static Map<Integer,String> portMaps = new HashMap<>();

     static {
         bannerMaps.put("ssh","SSH");
         bannerMaps.put("ftp","FTP");
         bannerMaps.put("smtp","SMTP");
         bannerMaps.put("mysql","MySQL");
         portMaps.put(20,"FTP");
         portMaps.put(21, "ftp/tftp/vsftpd");
         portMaps.put(22, "ssh");
         portMaps.put(23, "telnet");
         portMaps.put(25, "smtp");
         portMaps.put(53, "dns");
         portMaps.put(67, "dhcp");
         portMaps.put(68, "dhcp");
         portMaps.put(80, "http");
         portMaps.put(110, "pop3");
         portMaps.put(139, "Samba");
         portMaps.put(143, "imap");
         portMaps.put(161, "snmp");
         portMaps.put(389, "ldap");
         portMaps.put(443, "https");
         portMaps.put(445, "smb");
         portMaps.put(512, "Linux Rexec");
         portMaps.put(513, "Linux Rexec");
         portMaps.put(514, "Linux Rexec");
         portMaps.put(873, "rsync");
         portMaps.put(1080, "socket");
         portMaps.put(1352, "lotus domino");
         portMaps.put(1433, "mssql");
         portMaps.put(1521, "oracle");
         portMaps.put(2049, "nfs");
         portMaps.put(2181, "zookeeper");
         portMaps.put(2375, "docker remote api");
         portMaps.put(3306, "mysql");
         portMaps.put(3389, "rdp");
         portMaps.put(4848, "glassfish console");
         portMaps.put(5000, "sybase/DB2");
         portMaps.put(5432, "postgresql");
         portMaps.put(5632, "pcanywhere");
         portMaps.put(5900, "vnc");
         portMaps.put(6379, "redis");
         portMaps.put(7001, "weblogic");
         portMaps.put(7002, "weblogic");
         portMaps.put(8069, "zabbix");
         portMaps.put(8161, "activemq");
         portMaps.put(8080, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8081, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8082, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8083, "Jboss/Tomcat/Resin/socket/influxDB");
         portMaps.put(8084, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8085, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8086, "Jboss/Tomcat/Resin/socket/influxDB");
         portMaps.put(8087, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8088, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8089, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8090, "Jboss/Tomcat/Resin/socket");
         portMaps.put(8443, "web");
         portMaps.put(8888, "web");
         portMaps.put(9000, "fastcgi");
         portMaps.put(9090, "Websphere console");
         portMaps.put(9200, "elasticsearch");
         portMaps.put(9300, "elasticsearch");
         portMaps.put(11211, "memcached");
         portMaps.put(27017, "mongodb");
         portMaps.put(27018, "mongodb");
     }

    public ScanObject(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService() {
         // 先根据port判断服务类型
        if (portMaps.containsKey(this.port)){
            this.service = portMaps.get(this.port);
        }
        if (banner != null && !banner.equals("")){
            for (String key : bannerMaps.keySet()) {
              if (banner.toLowerCase().contains(key)) {
                  this.service = bannerMaps.get(key);
                  break;
              }
            }
        }
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;

    }

    @Override
    public String toString() {
        return "ScanObject{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", service='" + service + '\'' +
                ", isOpen=" + isOpen +
                ", banner='" + banner + '\'' +
                '}';
    }
}
