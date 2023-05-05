/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : loganalysis

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 05/05/2023 12:52:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for black_white_list
-- ----------------------------
DROP TABLE IF EXISTS `black_white_list`;
CREATE TABLE `black_white_list`  (
  `ORG_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '组织机构ID',
  `TYPE` int NOT NULL COMMENT '黑/白名单 1/2',
  `IP` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'IP地址',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`TYPE`, `IP`) USING BTREE,
  INDEX `ORG_ID`(`ORG_ID` ASC) USING BTREE,
  CONSTRAINT `black_white_list_ibfk_1` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`ORG_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '黑白名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of black_white_list
-- ----------------------------

-- ----------------------------
-- Table structure for block_off_record
-- ----------------------------
DROP TABLE IF EXISTS `block_off_record`;
CREATE TABLE `block_off_record`  (
  `BLOCK_OFF_RULE_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '封堵规则ID',
  `BLOCK_OFF_IP` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '封堵IP',
  `BLOCK_START_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '封堵开始时间',
  `BLOCK_END_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '封堵结束时间',
  `BLOCK_STATE` int NULL DEFAULT NULL COMMENT '封堵状态 1封堵中 2已解封',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`BLOCK_OFF_RULE_ID`, `BLOCK_OFF_IP`) USING BTREE,
  CONSTRAINT `block_off_record_ibfk_1` FOREIGN KEY (`BLOCK_OFF_RULE_ID`) REFERENCES `block_off_rule` (`BLOCK_OFF_RULE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '封堵历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of block_off_record
-- ----------------------------

-- ----------------------------
-- Table structure for block_off_rule
-- ----------------------------
DROP TABLE IF EXISTS `block_off_rule`;
CREATE TABLE `block_off_rule`  (
  `BLOCK_OFF_RULE_ID` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '封堵规则ID',
  `EQUIP_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '设备唯一编码',
  `SEVERITY` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日志级别',
  `FACILITY` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日志生产子系统',
  `IP_ITEM_NAME` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '来源IP字段名',
  `BLOCK_OFF_TIME` bigint NULL DEFAULT NULL COMMENT '封堵时长(秒)',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`EQUIP_ID`, `SEVERITY`, `FACILITY`) USING BTREE,
  UNIQUE INDEX `BLOCK_OFF_RULE_ID`(`BLOCK_OFF_RULE_ID` ASC) USING BTREE,
  CONSTRAINT `block_off_rule_ibfk_1` FOREIGN KEY (`EQUIP_ID`) REFERENCES `safety_equipment` (`EQUIP_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '封堵规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of block_off_rule
-- ----------------------------
INSERT INTO `block_off_rule` VALUES (00000000001, 00000000002, 'debug', '', '', NULL, '2023-05-05 12:50:19', NULL, '2023-05-05 12:50:19', NULL, NULL);
INSERT INTO `block_off_rule` VALUES (00000000002, 00000000002, 'debug', 'user', '', NULL, '2023-05-05 12:50:21', NULL, '2023-05-05 12:50:21', NULL, NULL);

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `LOG_ID` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '日志内容ID',
  `TIMESTAMP` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '时间戳',
  `HOST` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主机',
  `SEVERITY` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '日志级别',
  `FACILITY` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '日志发送方',
  `MESSAGE` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '日志消息',
  `TAG` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`LOG_ID`) USING BTREE,
  INDEX `SEARCH`(`HOST` ASC, `SEVERITY` ASC, `FACILITY` ASC) USING BTREE COMMENT '快速搜索'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '日志内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for log_analysis_rule
-- ----------------------------
DROP TABLE IF EXISTS `log_analysis_rule`;
CREATE TABLE `log_analysis_rule`  (
  `ANALYSIS_RULE_ID` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `RULE_TYPE` int NOT NULL DEFAULT 1 COMMENT '规则类型(1配置类规则 2硬编码规则)',
  `METHOD_NAME` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '硬编码方法名',
  `ITEM_SPLIT` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '分段字符',
  `KV_SPLIT` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '分值字符',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  UNIQUE INDEX `ANALYSIS_RULE_ID`(`ANALYSIS_RULE_ID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '日志解析规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log_analysis_rule
-- ----------------------------
INSERT INTO `log_analysis_rule` VALUES (00000000003, 1, '', ',', ':', '2023-05-05 12:45:56', NULL, '2023-05-05 12:45:56', NULL, NULL);

-- ----------------------------
-- Table structure for log_analysis_rule_rela
-- ----------------------------
DROP TABLE IF EXISTS `log_analysis_rule_rela`;
CREATE TABLE `log_analysis_rule_rela`  (
  `RULE_RELA_ID` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '关联关系ID',
  `EQUIP_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '设备唯一编码',
  `SEVERITY` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日志级别',
  `FACILITY` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日志生产子系统',
  `ANALYSIS_RULE_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '规则ID',
  `MERGE_ITEMS` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '归并字段(列表)',
  `WINDOW_TIME` bigint NOT NULL DEFAULT -1 COMMENT '归并窗口时长',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`EQUIP_ID`, `SEVERITY`, `FACILITY`, `ANALYSIS_RULE_ID`) USING BTREE,
  UNIQUE INDEX `RULE_RELA_ID`(`RULE_RELA_ID` ASC) USING BTREE,
  INDEX `ANALYSIS_RULE_ID`(`ANALYSIS_RULE_ID` ASC) USING BTREE,
  CONSTRAINT `log_analysis_rule_rela_ibfk_1` FOREIGN KEY (`ANALYSIS_RULE_ID`) REFERENCES `log_analysis_rule` (`ANALYSIS_RULE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `log_analysis_rule_rela_ibfk_2` FOREIGN KEY (`EQUIP_ID`) REFERENCES `safety_equipment` (`EQUIP_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '日志解析规则映射关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log_analysis_rule_rela
-- ----------------------------

-- ----------------------------
-- Table structure for log_index
-- ----------------------------
DROP TABLE IF EXISTS `log_index`;
CREATE TABLE `log_index`  (
  `RULE_RELA_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '关联关系ID',
  `UNION_KEY` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字段联合主键',
  `LOG_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '日志内容ID',
  PRIMARY KEY (`RULE_RELA_ID`, `UNION_KEY`, `LOG_ID`) USING BTREE,
  UNIQUE INDEX `log_id`(`LOG_ID` ASC) USING BTREE,
  CONSTRAINT `log_index_ibfk_1` FOREIGN KEY (`RULE_RELA_ID`) REFERENCES `log_analysis_rule_rela` (`RULE_RELA_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `log_index_ibfk_2` FOREIGN KEY (`LOG_ID`) REFERENCES `log` (`LOG_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '日志索引' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log_index
-- ----------------------------

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `ORG_ID` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `ORG_NAME` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `ORG_DESC` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组织描述',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`ORG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '组织机构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (00000000002, '测试组织机构', '描述', '2023-05-05 12:34:51', NULL, '2023-05-05 12:34:51', NULL, NULL);

-- ----------------------------
-- Table structure for safety_equipment
-- ----------------------------
DROP TABLE IF EXISTS `safety_equipment`;
CREATE TABLE `safety_equipment`  (
  `EQUIP_ID` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '安全设备ID',
  `IP` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'IP地址',
  `PORT` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '端口',
  `EQUIP_TYPE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备类型',
  `ORG_ID` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '所属组织机构',
  `EQUIP_NAME` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `EQUIP_DESC` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备描述',
  `ACCOUNT` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '远程登陆账号',
  `PASSWORD` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `EQUIP_STATE` int NULL DEFAULT NULL COMMENT '设备状态',
  `MANAGER` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `CREATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `UPDATE_BY` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `DELETE_STATE` int NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`IP`, `PORT`, `EQUIP_TYPE`) USING BTREE,
  UNIQUE INDEX `EQUIP_ID`(`EQUIP_ID` ASC) USING BTREE,
  INDEX `ORG_ID`(`ORG_ID` ASC) USING BTREE,
  CONSTRAINT `safety_equipment_ibfk_1` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`ORG_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '安全设备' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of safety_equipment
-- ----------------------------
INSERT INTO `safety_equipment` VALUES (00000000002, '192.168.43.14', '8080', '1', 00000000002, '测试安全设备(本机)', '', '', '', NULL, '', '2023-05-05 12:40:08', NULL, '2023-05-05 12:40:08', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
