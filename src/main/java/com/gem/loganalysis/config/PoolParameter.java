package com.gem.loganalysis.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author GuoChao
 */
@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "pool")
public class PoolParameter {

    @Value("${pool.poolName}")
    private String poolName;

    @Value("${pool.poolType}")
    private String poolType;

    @Value("${pool.dbType}")
    private String dbType;

    @Value("${pool.dbVersion}")
    private String dbVersion;

    @Value("${pool.driverName}")
    private String driverName;

    @Value("${pool.jndiName}")
    private String jndiName;

    @Value("${pool.jdbcURL}")
    private String jdbcURL;

    @Value("${pool.userName}")
    private String userName;

    @Value("${pool.password}")
    private String password;

    @Value("${pool.schema}")
    private String schema;

    @Value("${pool.initialSize}")
    private int initialSize;

    @Value("${pool.maxActive}")
    private int maxActive;

    @Value("${pool.maxIdle}")
    private int maxIdle;

    @Value("${pool.minIdle}")
    private int minIdle;

    @Value("${pool.maxWait}")
    private int maxWait;

    @Value("${pool.increaseStep}")
    private int increaseStep;

    @Value("${pool.inspectionIntervals}")
    private int inspectionIntervals;

    @Value("${pool.maxIdleTick}")
    private int maxIdleTick;

    @Value("${pool.validationQuery}")
    private String validationQuery;

    @Value("${pool.fetchSize}")
    private int fetchSize;

    @Value("${pool.sshIP}")
    private String sshIP;

    @Value("${pool.sshPort}")
    private int sshPort;

    @Value("${pool.sshUserName}")
    private String sshUserName;

    @Value("${pool.sshPassword}")
    private String sshPassword;

    @Value("${pool.localPort}")
    private int localPort;

    @Value("${pool.dbIP}")
    private String dbIP;

    @Value("${pool.dbPort}")
    private int dbPort;

    @Value("${pool.sshJdbcURL}")
    private String sshJdbcURL;

    @Value("${pool.testOnIdle}")
    private Boolean testOnIdle;

    @Value("${pool.passwordEncrypted}")
    private Boolean passwordEncrypted;

}
