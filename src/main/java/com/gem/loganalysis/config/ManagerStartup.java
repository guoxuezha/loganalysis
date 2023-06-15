package com.gem.loganalysis.config;

import com.gem.gemada.dal.db.pools.ConnectionPools;
import com.gem.gemada.dal.db.pools.DAO;
import com.gem.gemada.dal.db.pools.PoolParameters;
import com.gem.loganalysis.kafka.KafkaAutoTableHandler;
import com.gem.loganalysis.snmpmonitor.SNMPMonitorServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author GuoChao
 */
@Component
@Slf4j
@AllArgsConstructor
@Order(1)
public class ManagerStartup implements CommandLineRunner {

    private final PoolParameter poolParameter;

    private final BusinessConfigInfo businessConfigInfo;

    private final KafkaAutoTableHandler kafkaAutoTableHandler;

    @Override
    public void run(String... args) {
        PoolParameters pool = new PoolParameters();
        pool.passwordEncrypted = poolParameter.getPasswordEncrypted();
        pool.poolName = poolParameter.getPoolName();
        pool.poolType = poolParameter.getPoolType();
        pool.dbType = poolParameter.getDbType();
        pool.dbVersion = poolParameter.getDbVersion();
        pool.driverName = poolParameter.getDriverName();
        pool.jndiName = poolParameter.getJndiName();
        pool.jdbcURL = poolParameter.getJdbcURL();
        pool.userName = poolParameter.getUserName();
        pool.password = poolParameter.getPassword();
        pool.schema = poolParameter.getSchema();
        pool.initialSize = poolParameter.getInitialSize();
        pool.maxActive = poolParameter.getMaxActive();
        pool.maxIdle = poolParameter.getMaxIdle();
        pool.minIdle = poolParameter.getMinIdle();
        pool.maxWait = poolParameter.getMaxWait();
        pool.increaseStep = poolParameter.getIncreaseStep();
        pool.inspectionIntervals = poolParameter.getInspectionIntervals();
        pool.maxIdleTick = poolParameter.getMaxIdleTick();
        pool.cacheSystemPool = false;
        pool.validationQuery = null;
        pool.testOnBorrow = true;
        pool.testOnReturn = false;
        pool.testOnIdle = poolParameter.getTestOnIdle();
        pool.fetchSize = poolParameter.getFetchSize();
        pool.useSsh = false;
        pool.sshIP = poolParameter.getSshIP();
        pool.sshPort = poolParameter.getSshPort();
        pool.sshUserName = poolParameter.getSshUserName();
        pool.sshPassword = poolParameter.getSshPassword();
        pool.localPort = poolParameter.getLocalPort();
        pool.dbIP = poolParameter.getDbIP();
        pool.dbPort = poolParameter.getDbPort();
        pool.sshJdbcURL = poolParameter.getSshJdbcURL();

        // DAL数据库连接池初始化
        ConnectionPools connectionPools = ConnectionPools.getInstance();
        connectionPools.createRepositoryConnPool(pool);
//        instance.createConnPools();

        // Gem-utils 调度器启动
//        Schedule.getInstance().start();

        if (businessConfigInfo.getSnmpMonitorEnable()) {
            SNMPMonitorServer.getInstance().createThreadPool();
        }

        // 初始化子Topic监听线程
        kafkaAutoTableHandler.initTopicListener();
    }

    @Bean
    public DAO dao() {
        return new DAO();
    }

}
