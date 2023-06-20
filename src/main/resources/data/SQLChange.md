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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='日志解析定制化规则';
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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='调度任务-工作';

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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='调度任务-子任务';

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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='调度任务-任务执行记录';
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
    OUTPUT_PARAM   VARCHAR(8)  NULL DEFAULT 'fn' COMMENT 'SNMP -O',
    ASSET_ID       VARCHAR(32) NULL COMMENT '资产编码',
    PRIMARY KEY (ASSET_ORG, IP_ADDRESS)
) COMMENT = 'SNMP参数设置';

DROP TABLE IF EXISTS SOP_MIB_CONFIG;
CREATE TABLE SOP_MIB_CONFIG
(
    MIB_VERSION  VARCHAR(64) NOT NULL COMMENT 'MIB版本',
    OID          VARCHAR(64) NOT NULL COMMENT 'SNMP命令要发送的OID',
    SNMP_METHOD  VARCHAR(16) NOT NULL COMMENT '命令发送方式，SNMPGET/SNMPWALK',
    MEASURE_TYPE VARCHAR(64) NOT NULL COMMENT '对应指标分类',
    PRIMARY KEY (MIB_VERSION, OID)
) COMMENT = 'MIB指令集设置';

DROP TABLE IF EXISTS SOP_COMMON_OID;
CREATE TABLE SOP_COMMON_OID
(
    MIB_VERSION  VARCHAR(32)  NOT NULL DEFAULT 'COMMON' COMMENT 'MIB版本',
    OID          VARCHAR(64)  NOT NULL COMMENT 'OID',
    OID_NAME     VARCHAR(32)  NOT NULL COMMENT 'OID名称',
    MEASURE_NAME VARCHAR(64)  NOT NULL COMMENT '对应指标名称',
    OID_DATATYPE VARCHAR(16)  NOT NULL DEFAULT 'STRING' COMMENT '数据类型',
    OID_DESC     VARCHAR(512) NULL COMMENT 'OID说明',
    PRIMARY KEY (MIB_VERSION, OID)
) COMMENT = '通用MIB库OID列表设置';
```

### 2023-05-30

#### 封堵实现调整

```SQL
ALTER TABLE `loganalysis`.`sop_block_rule`
    ADD COLUMN `OPERATION_ASSET_ID` varchar(512) NULL COMMENT '执行操作的资产(防火墙)ID(若有多个则使用,分割)' AFTER `BLACK_LIST_ENABLE`;

```

### 2023-06-07

#### 资产封堵命令表

```SQL
CREATE TABLE `sop_block_command`
(
    `ASSET_ID`        varchar(32) NOT NULL COMMENT '资产唯一编码',
    `BLOCK_COMMAND`   varchar(255) DEFAULT NULL COMMENT '封堵命令模板',
    `DEBLOCK_COMMAND` varchar(255) DEFAULT NULL COMMENT '解封命令模板',
    PRIMARY KEY (`ASSET_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='封堵命令配置';
```

### 2023-06-08

#### 日志解析属性映射

```SQL
CREATE TABLE `sop_log_normal_form`
(
    `FIELD_ID`     varchar(16) NOT NULL COMMENT '日志项ID',
    `FIELD_NAME`   varchar(32)                                                  DEFAULT NULL COMMENT '日志项名称(Key)',
    `FIELD_DESC`   varchar(255)                                                 DEFAULT NULL COMMENT '日志项描述(中文)',
    `PID`          varchar(16)                                                  DEFAULT NULL COMMENT '父节点ID',
    `LEVEL`        int                                                          DEFAULT NULL COMMENT '所属层级',
    `CREATE_TIME`  varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建时间',
    `CREATE_BY`    varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建者',
    `UPDATE_TIME`  varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改时间',
    `UPDATE_BY`    varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `DELETE_STATE` int                                                          DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`FIELD_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='日志范式';

CREATE TABLE `sop_log_field_mapping`
(
    `RULE_RELA_ID`      varchar(32)                                                  NOT NULL COMMENT '日志解析规则关联关系编码',
    `SOURCE_FIELD_NAME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '源端日志项名称',
    `SOURCE_FIELD_DESC` varchar(255)                                                 DEFAULT NULL COMMENT '源端日志项描述',
    `TARGET_FIELD_ID`   varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目标日志项ID',
    `CREATE_TIME`       varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建时间',
    `CREATE_BY`         varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建者',
    `UPDATE_TIME`       varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改时间',
    `UPDATE_BY`         varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
    `DELETE_STATE`      int                                                          DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`RULE_RELA_ID`, `SOURCE_FIELD_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='日志字段映射';

```

#### 旧模型调整 - 封堵规则关联到风险,而非事件

```SQL
ALTER TABLE `loganalysis`.`sop_block_rule`
    ADD COLUMN `ASSET_ID`    varchar(32) NULL COMMENT '资产ID' AFTER `BLOCK_RULE_ID`,
    ADD COLUMN `RULE_TYPE`   int         NULL COMMENT '规则类型（0按风险级别/1按IP归属地）' AFTER `BLOCK_RULE_DESC`,
    ADD COLUMN `RISK_LEVEL`  int         NULL COMMENT '风险级别（1/2/3 低危/中危/高危）' AFTER `RULE_TYPE`,
    ADD COLUMN `BLOCK_RANGE` int         NULL COMMENT '封堵范围（1/2 国外/省外）' AFTER `RISK_LEVEL`;

ALTER TABLE `loganalysis`.`sop_event_type`
    DROP
        COLUMN `BLOCK_RULE_ID`,
    DROP
        PRIMARY KEY,
    ADD PRIMARY KEY (`EVENT_TYPE`, `EVENT_CLASS`) USING BTREE;

ALTER TABLE `loganalysis`.`sop_asset_risk`
    ADD COLUMN `RISK_LEVEL` int NULL COMMENT '风险等级(1/2/3 低危/中危/高危)' AFTER `SCAN_TIME`;
```

#### 原始日志记录预览

```SQL
CREATE TABLE `sop_asset_log_preview`
(
    `HOST`      varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主机IP',
    `SEVERITY`  varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日志优先级',
    `FACILITY`  varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日志发生的子系统',
    `TIMESTAMP` varchar(64)                                                    DEFAULT NULL COMMENT '时间戳',
    `MESSAGE`   varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '日志消息体',
    `TAG`       varchar(255)                                                   DEFAULT NULL COMMENT '消息标签'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='资产日志预览';
```

### 2023-06-09

#### 事件记录保留源IP与目标IP

```SQL
ALTER TABLE `loganalysis`.`sop_asset_event`
    ADD COLUMN `TARGET_IP` varchar(32) NULL COMMENT '目标IP' AFTER `SOURCE_IP`;
```

### 2023-06-11

#### 日志范式属性添加Tag字段,用于标记特殊属性

```SQL
ALTER TABLE `loganalysis`.`sop_log_normal_form`
    ADD COLUMN `FIELD_TAG` int NULL COMMENT '属性标签 (1/2/3/4 源IP/目标IP/事件类型/危险等级)' AFTER `LEVEL`;
```

### 2023-06-12

#### 日志解析规则表字段精简

```SQL
ALTER TABLE `loganalysis`.`sop_log_analysis_rule_rela`
    DROP COLUMN `EVENT_KEYWORD`,
    DROP COLUMN `EVENT_TYPE_ITEM`,
    DROP COLUMN `EVENT_CLASS_ITEM`,
    MODIFY COLUMN `MERGE_ITEMS` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '归并字段属性列表' AFTER `ANALYSIS_RULE_ID`;

ALTER TABLE `loganalysis`.`sop_block_rule`
    MODIFY COLUMN `BLOCK_RULE_DESC` varchar(128) CHARACTER
        SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '封堵规则描述' AFTER `ASSET_ID`,
    MODIFY COLUMN `RULE_TYPE` int NOT NULL COMMENT '规则类型（0按风险级别/1按IP归属地）' AFTER `BLOCK_RULE_DESC`,
    MODIFY COLUMN `BLOCK_DURATION` int NULL DEFAULT 30 COMMENT '临时封堵时长（分钟）' AFTER `BLOCK_TYPE`;
```

### 2023-06-14

#### 日志解析规则分步配置,去除非空约束

```SQL
ALTER TABLE `loganalysis`.`sop_log_analysis_rule_rela`
    MODIFY COLUMN `MERGE_WINDOW_TIME` int NULL DEFAULT 60 COMMENT '归并窗口时长（分钟）' AFTER `MERGE_ITEMS`,
    MODIFY COLUMN `EVENT_WINDOW_TIME` int NULL DEFAULT 1 COMMENT '事件判定窗口时长（分钟）' AFTER `MERGE_WINDOW_TIME`;
```

### 2023-06-20

#### 增加逻辑资产推测类型长度，并且增加字段TYPE（0为自动扫描 1为手动扫描）

```SQL
ALTER TABLE loganalysis.sop_logical_asset_temp
    MODIFY COLUMN ASSET_TYPE VARCHAR(32),
    ADD COLUMN TYPE INT COMMENT '扫描类型(0为自动 1为手动)';

ALTER TABLE loganalysis.sop_physical_asset_temp
    ADD COLUMN TYPE INT COMMENT '扫描类型(0为自动 1为手动)';
```