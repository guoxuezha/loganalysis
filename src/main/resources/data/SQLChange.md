### 2023-05-15 郭超

#### 日志解析规则增加字段-规则名称

```SQL
ALTER TABLE `loganalysis`.`sop_log_analysis_rule`
    ADD COLUMN `ANALYSIS_RULE_NAME` varchar(64) NULL COMMENT '规则名称' AFTER `ANALYSIS_RULE_ID`;
```

#### 新建定制化日志解析规则信息表

```SQL
CREATE TABLE `sop_log_analysis_customization_rule`
(
    `METHOD_NAME` varchar(64) NOT NULL COMMENT '硬编码方法名',
    `JAR_NAME`    varchar(64) DEFAULT NULL COMMENT '所属Jar包名',
    `VERSION`     varchar(8)  DEFAULT NULL COMMENT '版本号',
    `UPDATE_TIME` varchar(14) DEFAULT NULL COMMENT 'Jar包更新时间',
    PRIMARY KEY (`METHOD_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='日志解析定制化规则';
```

#### 删除标记设置默认值为0

```SQL
ALTER TABLE `loganalysis`.`sop_asset`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_asset_group`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_black_list`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_block_rule`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_event_type`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_log_analysis_rule`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_log_analysis_rule_rela`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;

ALTER TABLE `loganalysis`.`sop_white_list`
    MODIFY COLUMN `DELETE_STATE` int NULL DEFAULT 0 COMMENT '删除标记' AFTER `UPDATE_BY`;
```

### 2023-05-23 郭超

#### 日志解析规则新增字段 - 识别事件类型和级别

```SQL
ALTER TABLE `loganalysis`.`sop_log_analysis_rule_rela`
    ADD COLUMN `EVENT_TYPE_ITEM` varchar(32) NULL COMMENT '事件类型字段' AFTER `EVENT_KEYWORD`;

ALTER TABLE `loganalysis`.`sop_log_analysis_rule_rela`
    ADD COLUMN `EVENT_CLASS_ITEM` varchar(32) NULL COMMENT '事件级别字段' AFTER `EVENT_TYPE_ITEM`;
```

### 2023-05-26 郭超

#### 引入调度任务

```SQL
CREATE TABLE `sop_job`
(
    `JOB_ID`           varchar(32) NOT NULL COMMENT '工作ID',
    `JOB_NAME`         varchar(255)                                                 DEFAULT NULL COMMENT '工作名称',
    `EXECUTE_ON_START` int         NOT NULL                                         DEFAULT '0' COMMENT '是否启动执行(0否 1是)',
    `FIRST_FIRED_TIME` varchar(14)                                                  DEFAULT NULL COMMENT '首次执行时间',
    `LAST_FIRED_TIME`  varchar(14)                                                  DEFAULT NULL COMMENT '上次执行时间',
    `NEXT_FIRED_TIME`  varchar(14)                                                  DEFAULT NULL COMMENT '下次执行时间',
    `CYCLE_DEFINE`     varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '执行周期定义(为遵循固定格式的字符串)',
    `CURRENT_STATUS`   int         NOT NULL                                         DEFAULT '-1' COMMENT '工作当前状态 -1: 未就绪 0: 就绪 1: 执行中',
    `COUNTS`           int                                                          DEFAULT NULL COMMENT '工作已执行次数',
    `CREATE_TIME`      varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建时间',
    `CREATE_BY`        varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建者',
    `UPDATE_TIME`      varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改时间',
    `UPDATE_BY`        varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `DELETE_STATE`     int                                                          DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='调度任务-工作';

CREATE TABLE `sop_task`
(
    `TASK_ID`      varchar(32)                                                 NOT NULL COMMENT '任务ID',
    `JOB_ID`       varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '工作ID',
    `TASK_NAME`    varchar(255)                                                 DEFAULT NULL COMMENT '任务名称',
    `TASK_DESC`    varchar(255)                                                 DEFAULT NULL COMMENT '任务描述',
    `CALL_MODE`    varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '调用模式(空/STATIC/URL/SERVLET/WEBSERVICE)',
    `CLASS_NAME`   varchar(255)                                                 DEFAULT NULL COMMENT '类名',
    `METHOD_NAME`  varchar(255)                                                 DEFAULT NULL COMMENT '方法名',
    `URL`          varchar(255)                                                 DEFAULT NULL COMMENT '调用路径',
    `CONTENT`      varchar(255)                                                 DEFAULT NULL COMMENT 'ServiceContent',
    `CREATE_TIME`  varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建时间',
    `CREATE_BY`    varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建者',
    `UPDATE_TIME`  varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改时间',
    `UPDATE_BY`    varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `DELETE_STATE` int                                                          DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`TASK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='调度任务-子任务';

CREATE TABLE `sop_task_log`
(
    `TASK_LOG_ID`     int(16) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '任务执行记录流水号',
    `TASK_ID`         varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '任务ID',
    `TASK_START_TIME` varchar(14)                                                  DEFAULT NULL COMMENT '任务开始时间',
    `TASK_END_TIME`   varchar(14)                                                  DEFAULT NULL COMMENT '任务结束时间',
    `EXECUTE_STATUS`  int                                                          DEFAULT NULL COMMENT '1成功 0失败',
    `EXECUTE_MESSAGE` varchar(255)                                                 DEFAULT NULL COMMENT '执行消息',
    `JOB_ID`          varchar(32)                                                  DEFAULT NULL COMMENT '工作ID(冗余)',
    PRIMARY KEY (`TASK_LOG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='调度任务-任务执行记录';
```

### 2023-05-29

#### 拓展SNMP相关模块

```SQL
ALTER TABLE `loganalysis`.`sop_asset`
    ADD COLUMN `MIB_VERSION` varchar(64) NULL COMMENT 'MIB版本' AFTER `SOFTWARE_VERSION`;

DROP TABLE IF EXISTS SOP_ASSET_SNMP_CONFIG;
CREATE TABLE SOP_ASSET_SNMP_CONFIG
(
    ASSET_ORG      VARCHAR(32) NOT NULL COMMENT '资产所属组织（部门）',
    IP_ADDRESS     VARCHAR(32) NOT NULL COMMENT 'IP地址(如非缺省端口则需带端口号，如172.168.1.1:10161)',
    SNMP_VERSION   VARCHAR(8)  NOT NULL COMMENT 'SNMP版本',
    SNMP_COMMUNITY VARCHAR(64) NULL COMMENT 'SNMP-V1/2c COMMUNITY',
    SNMP_USERNAME  VARCHAR(64) NULL COMMENT 'SNMP-V3 USERNAME',
    AUTH_PASSWORD  VARCHAR(64) NULL COMMENT 'SNMP-V3 AUTH_PASSWORD',
    PRIV_PASSWORD  VARCHAR(64) NULL COMMENT 'SNMP-V3 PRIV_PASSWORD',
    OUTPUT_PARAM   VARCHAR(8) NULL DEFAULT 'fn' COMMENT 'SNMP -O',
    ASSET_ID       VARCHAR(32) NULL COMMENT '资产编码',
    PRIMARY KEY (ASSET_ORG, IP_ADDRESS)
)COMMENT = 'SNMP参数设置';

DROP TABLE IF EXISTS SOP_MIB_CONFIG;
CREATE TABLE SOP_MIB_CONFIG
(
    MIB_VERSION  VARCHAR(64) NOT NULL COMMENT 'MIB版本',
    OID          VARCHAR(64) NOT NULL COMMENT 'SNMP命令要发送的OID',
    SNMP_METHOD  VARCHAR(16) NOT NULL COMMENT '命令发送方式，SNMPGET/SNMPWALK',
    MEASURE_TYPE VARCHAR(64) NOT NULL COMMENT '对应指标分类',
    PRIMARY KEY (MIB_VERSION, OID)
)COMMENT = 'MIB指令集设置';

DROP TABLE IF EXISTS SOP_COMMON_OID;
CREATE TABLE SOP_COMMON_OID
(
    MIB_VERSION  VARCHAR(32) NOT NULL DEFAULT 'COMMON' COMMENT 'MIB版本',
    OID          VARCHAR(64) NOT NULL COMMENT 'OID',
    OID_NAME     VARCHAR(32) NOT NULL COMMENT 'OID名称',
    MEASURE_NAME VARCHAR(64) NOT NULL COMMENT '对应指标名称',
    OID_DATATYPE VARCHAR(16) NOT NULL DEFAULT 'STRING' COMMENT '数据类型',
    OID_DESC     VARCHAR(512) NULL COMMENT 'OID说明',
    PRIMARY KEY (MIB_VERSION, OID)
)COMMENT = '通用MIB库OID列表设置';
```