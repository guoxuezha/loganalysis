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