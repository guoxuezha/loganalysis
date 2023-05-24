package com.gem.loganalysis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
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
     * 单点登录默认packageId
     */
    private String adadDefaultPackage;

}
