package com.gem.loganalysis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 业务配置信息
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/22 16:41
 */
@Data
@Component
@ConfigurationProperties(value = "business-config")
public class BusinessConfigInfo {

    /**
     * 是否启用认证拦截
     */
    private Boolean authenticationEnable;

    /**
     * 是否启用入参报文加密
     */
    private Boolean responseEncryptEnable;

    /**
     * AES加解密密钥
     */
    private String AESKey;

    /**
     * 是否开启日志监听
     */
    private Boolean logMonitorEnable;

    /**
     * SNMP扫描开启
     */
    private Boolean snmpMonitorEnable;

    /**
     * 单点登录默认packageId
     */
    private String adadDefaultPackage;

    /**
     * 原始日志文件保留天数
     */
    private Integer logBlockFileRetentionDays;

    /**
     * webSocket前缀
     */
    private String webSocketPrefix;

    /**
     * 漏洞扫描代理客户端是否启用
     */
    private Boolean gSAClientAgentEnable;

    /**
     * 境外访问判断
     */
    private Boolean overseasVisitJudgment;

}
