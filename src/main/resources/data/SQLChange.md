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