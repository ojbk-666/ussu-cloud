/*
 Navicat Premium Data Transfer

 Source Server         : localhost_mysql8.0
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3307
 Source Schema         : ussu-vue

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 03/06/2021 18:44:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dc_course
-- ----------------------------
DROP TABLE IF EXISTS `dc_course`;
CREATE TABLE `dc_course`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `course_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_attr_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_attr_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_fee` int(0) NULL DEFAULT NULL,
  `course_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_kind_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_kind_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_status_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `course_term` int(0) NULL DEFAULT NULL,
  `course_type_id` int(0) NULL DEFAULT NULL,
  `courseware_down_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `courseware_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cx_num` int(0) NULL DEFAULT NULL,
  `daoxue_flag` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exam_method_group_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exam_num` int(0) NULL DEFAULT NULL,
  `mianxiu_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `score_have_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `service_course_vers_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stop_flag` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `study_credit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `subject_course_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `interface_log_id` bigint(0) UNSIGNED NULL DEFAULT NULL,
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1123 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dc_interface_log
-- ----------------------------
DROP TABLE IF EXISTS `dc_interface_log`;
CREATE TABLE `dc_interface_log`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '页面访问的url',
  `req_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求url',
  `access_token` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'accesstoken',
  `userid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'userid',
  `sign` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名',
  `time` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与签名计算',
  `response_body` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '响应体',
  `result` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否成功',
  `reason` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '删除标记',
  `remarks` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6295 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接口日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dc_paper
-- ----------------------------
DROP TABLE IF EXISTS `dc_paper`;
CREATE TABLE `dc_paper`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `paper_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'PAPER_ID',
  `paper_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `interface_log_id` bigint(0) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1777 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'paper' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dc_paper_question
-- ----------------------------
DROP TABLE IF EXISTS `dc_paper_question`;
CREATE TABLE `dc_paper_question`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `paper_id_long` bigint(0) UNSIGNED NULL DEFAULT NULL,
  `paper_id_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `topic_id_long` bigint(0) UNSIGNED NULL DEFAULT NULL,
  `topic_id_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `question_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QUESTION_ID',
  `question_title` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QUESTION_TITLE',
  `topictrunk_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TOPICTRUNK_TYPE',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `interface_log_id` bigint(0) UNSIGNED NOT NULL COMMENT '接口id',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `source` int(0) UNSIGNED NULL DEFAULT 10 COMMENT '来源 10接口日志 20手动录入',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12773 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dc_paper_question_topic
-- ----------------------------
DROP TABLE IF EXISTS `dc_paper_question_topic`;
CREATE TABLE `dc_paper_question_topic`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `paper_id_long` bigint(0) UNSIGNED NULL DEFAULT NULL,
  `paper_id_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `topic_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TOPIC_ID',
  `topic_type_cd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TOPIC_TYPE_CD',
  `full_topic_type_cd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Fulltopictypecd',
  `question_type_nm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QUESTION_TYPE_NM',
  `topic_type_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TOPIC_TYPE_ID',
  `interface_log_id` bigint(0) NOT NULL COMMENT '接口id',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1015 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dc_question_option
-- ----------------------------
DROP TABLE IF EXISTS `dc_question_option`;
CREATE TABLE `dc_question_option`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `paper_id_long` bigint(0) UNSIGNED NULL DEFAULT NULL,
  `paper_id_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `question_id_long` bigint(0) UNSIGNED NOT NULL COMMENT '题目表主键',
  `question_id_str` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目表question_id',
  `option_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'OPTION_ID',
  `option_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OPTION_CONTENT',
  `option_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OPTION_TYPE',
  `istrue` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `selected` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'selected',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `interface_log_id` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '接口id',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42675 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目的选项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dc_user_info
-- ----------------------------
DROP TABLE IF EXISTS `dc_user_info`;
CREATE TABLE `dc_user_info`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `accept_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `card_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `card_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `certificate_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `commadd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通讯地址',
  `company_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作单位',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fdz_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学习中心',
  `gra_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业日期',
  `graduate_school` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `pre_major` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业专业',
  `rx_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入学批次',
  `sch_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `stu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `student_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `study_kind` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '层次',
  `study_mode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stufile_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stufile_status_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `subject_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业',
  `tp_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `zip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `interface_log_id` bigint(0) UNSIGNED NOT NULL COMMENT '接口id',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学生基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dc_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for dingtalk_msg_log
-- ----------------------------
DROP TABLE IF EXISTS `dingtalk_msg_log`;
CREATE TABLE `dingtalk_msg_log`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `access_token` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'accesstoken',
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'userid',
  `dd_userid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `dd_agentid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'agentid',
  `msgtype` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msgcontent` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `response_body` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '响应体',
  `result` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否成功',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '删除标记',
  `remarks` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '钉钉推送消息接口日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dingtalk_msg_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area`  (
  `id` int(0) NOT NULL COMMENT '区域主键',
  `code` int(0) NULL DEFAULT NULL COMMENT '区域编码，身份证号前6位',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '区域上级标识',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'id路径',
  `simple_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地名简称',
  `level` int(0) NULL DEFAULT NULL COMMENT '区域等级',
  `citycode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区号，即固定电话前拼接的',
  `yzcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `mername` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组合名称',
  `lng` float NULL DEFAULT NULL COMMENT '经度',
  `lat` float NULL DEFAULT NULL COMMENT '纬度',
  `pinyin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼音',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除 1删除 0正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_mername`(`mername`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '省市区区域表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_attach
-- ----------------------------
DROP TABLE IF EXISTS `sys_attach`;
CREATE TABLE `sys_attach`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件组唯一ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件（字节）',
  `file_ext` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `attach_sort` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  `attach_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'default' COMMENT '模块代码',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除：1，是；0，否',
  `create_dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_attachment_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_date_line
-- ----------------------------
DROP TABLE IF EXISTS `sys_date_line`;
CREATE TABLE `sys_date_line`  (
  `date_date` date NOT NULL COMMENT '日期 日期类型 2020-01-04',
  `date_str` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日期 字符串类型 2020-01-04',
  `month_str` char(7) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '月 字符串类型 2021-01',
  UNIQUE INDEX `idx_unique_date_date`(`date_date`) USING BTREE,
  UNIQUE INDEX `idx_unique_date_str`(`date_str`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级部门',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门简称',
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门全称',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门电话',
  `sort` int(0) NULL DEFAULT NULL COMMENT '部门排序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门路径(/总部/山东/济南/综合部)',
  `level` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '部门级别',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除：1，是；0，否',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  `create_dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_path`(`path`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `dict_value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据值',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
  `type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型id',
  `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `dict_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `dict_sort` int(0) UNSIGNED NOT NULL DEFAULT 10 COMMENT '排序',
  `status` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态 1正常 0停用',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记：0正常  1删除',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典编码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `status` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态 1正常 0停用',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记：0正常  1删除',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_dict_type_status`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_email
-- ----------------------------
DROP TABLE IF EXISTS `sys_email`;
CREATE TABLE `sys_email`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `sender_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方账号',
  `sender_nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送昵称',
  `receiver_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收账号',
  `receiver_nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收昵称',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `result` tinyint(0) UNSIGNED NULL DEFAULT 1 COMMENT '发送结果',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_email
-- ----------------------------
INSERT INTO `sys_email` VALUES ('123', 'useeseeu@163.com', '123', '123@qq.com', '123qq', '测试', '二十邮件测试', 1, NULL, '2020-07-16 18:23:25', 0);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最近一次运行时间',
  `task_group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'DEFAULT_GROUP' COMMENT '任务组名称',
  `task_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `target_class` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调用目标类',
  `cron` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '周期表达式',
  `misfire_policy` int(0) UNSIGNED NULL DEFAULT 3 COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否并发执行（1允许 0禁止）',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `stop_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '停止状态：0否 1是',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除状态 0正常 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '作业监控' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名',
  `job_group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `target_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标类',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '执行开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '执行结束时间',
  `cost_time` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '耗时(毫秒)',
  `once_job` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '执行一次任务',
  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `exec_success` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '执行结果 1成功 0失败',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统作业运行日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `log_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式:get,post,delete,put',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `request_params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `request_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '请求发起时间',
  `http_status` int(0) NOT NULL DEFAULT 200 COMMENT 'http状态码:200,404,500等',
  `result_code` int(0) NULL DEFAULT 0 COMMENT '业务响应状态码，code',
  `result_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务响应状态消息，msg',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `browser_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原始的浏览器ua信息',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户的userid',
  `remote_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端ip',
  `execute_time` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '请求耗时',
  `exception_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常堆栈信息',
  `location_str` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `location_adcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uuid',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'token 缓存中的键值',
  `authorization` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'authorization',
  `login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '登录时间',
  `logout_time` datetime(0) NULL DEFAULT NULL COMMENT '退出时间',
  `login_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录时的ip',
  `location_str` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录位置',
  `location_adcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录位置代码',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录时使用的设备名称',
  `browser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录时使用的浏览器',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细的ua信息',
  `login_result` tinyint(0) UNSIGNED NULL DEFAULT 1 COMMENT '登录是否成功1成功 0失败',
  `login_failed_reason` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '删除标记 0正常 1删除',
  `create_dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10785 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户会话记录/登录日志 表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级编号，一级菜单0',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  `type` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '菜单类型（10菜单 20权限）',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `show_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否在菜单中隐藏：1，1隐藏；0，显示；',
  `iframe_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否外链 0否 1是',
  `cache_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否缓存',
  `perm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `sys_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统分类，取字典',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '1正常 0停用',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除：1，是；0，否',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1172397462871080962', '2019040916000705483768', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:user:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172397771341168642', '2019040916000705483768', '编辑', 302, 30, NULL, '', '', 1, 0, 0, 'system:user:edit', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172399403512627201', '2019040916000705483768', '删除', 303, 30, NULL, '', '', 1, 0, 0, 'system:user:delete', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172399810032959490', '2019082319514900567416', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:role:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172399893512192002', '2019082319514900567416', '编辑', 302, 30, NULL, '', '', 1, 0, 0, 'system:role:edit', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172418580801667074', '2019040916030120163963', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:param:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172418673470619649', '2019040916030120163963', '编辑', 302, 30, NULL, '', '', 1, 0, 0, 'system:param:edit', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1172418760242380801', '2019040916030120163963', '删除', 303, 30, NULL, '', '', 1, 0, 0, 'system:param:delete', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1174126024312606722', '2019040915563913037130', '菜单管理', 240, 20, 'sys-menu', 'system/menu/index', 'yishouquan', 1, 0, 0, '', 'system', '', 1, 0, 6);
INSERT INTO `sys_menu` VALUES ('1174294687699226625', '1174126024312606722', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:menu:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1174294828191633409', '1174126024312606722', '新增', 302, 30, NULL, '', '', 1, 0, 0, 'system:menu:add', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1174294922366341121', '1174126024312606722', '删除', 303, 30, NULL, '', '', 1, 0, 0, 'system:menu:delete', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1175249447425155074', '2019082319514900567416', '删除', 303, 30, NULL, '', '', 1, 0, 0, 'system:role:delete', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176058207642161154', '2019040915563913037130', '系统日志', 300, 20, 'syslog', 'system/log/index', 'monitor', 1, 0, 0, '', 'system', '', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('1176058542314065922', '1176058207642161154', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:log:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176369670478954498', '2019040915563913037130', '作业调度', 270, 20, 'sysjob', 'system/job/index', 'date-range', 1, 0, 0, '', 'system', '', 0, 0, 4);
INSERT INTO `sys_menu` VALUES ('1176369795083337730', '1176369670478954498', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:job:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176392001364529154', '1176369670478954498', '编辑', 302, 30, NULL, '', '', 1, 0, 0, 'system:job:edit', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176392119711010817', '1176369670478954498', '删除', 303, 30, NULL, '', '', 1, 0, 0, 'system:job:delete', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176432027993489410', '1280040423720988674', '清空日志', 302, 30, NULL, '', '', 1, 0, 0, 'system:log:delete', 'system', '', 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1176474985035800577', '1176369670478954498', '查看调度日志', 304, 30, NULL, '', '', 1, 0, 0, 'system:job:select', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176661455696564225', '2019082319514900567416', '查看角色人员', 304, 30, NULL, '', '', 1, 0, 0, 'system:role:users', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176661643760766978', '2019082319514900567416', '修改权限', 305, 30, NULL, '', '', 1, 0, 0, 'system:role:perms', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176668377187356673', '2019082319514900567416', '导出', 306, 30, NULL, '', '', 1, 0, 0, 'system:role:export', 'system', '', 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1176791428973494274', '2019040916024028763683', '查看字典类型', 304, 30, NULL, '', '', 1, 0, 0, 'system:dicttype:select', 'system', '', 1, 0, 6);
INSERT INTO `sys_menu` VALUES ('1177547490496475138', '2019040915563913037130', '在线用户', 290, 20, 'sysonline', 'system/online/index', 'people', 1, 0, 0, '', 'system', '', 0, 0, 8);
INSERT INTO `sys_menu` VALUES ('1200429538887127042', '2019040915563913037130', '登录日志', 280, 20, 'sysloginlog', 'system/loginlog/index', 'zhanghaoquanxianguanli', 1, 0, 0, '', 'system', '', 0, 0, 9);
INSERT INTO `sys_menu` VALUES ('1200429757645246465', '1200429538887127042', '查看', 301, 30, NULL, '', '', 1, 0, 0, 'system:login-log:select', 'system', '', 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1210904323656040450', '2019081420522352867520', '查看', 310, 30, NULL, '', '', 1, 0, 0, 'system:area:select', 'system', '', 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1210904558797111298', '2019081420522352867520', '编辑', 320, 30, NULL, '', '', 1, 0, 0, 'system:area:edit', 'system', '', 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1210904707019620353', '2019081420522352867520', '删除', 330, 30, NULL, '', '', 1, 0, 0, 'system:area:delete', 'system', '', 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1218047923753848833', '2019040916024028763683', '编辑字典类型', 305, 30, NULL, '', '', 1, 0, 0, 'system:dicttype:edit', 'system', '', 1, 0, 4);
INSERT INTO `sys_menu` VALUES ('1218047995585499137', '2019040916024028763683', '删除字典类型', 306, 30, NULL, '', '', 1, 0, 0, 'system:dicttype:delete', 'system', '', 1, 0, 4);
INSERT INTO `sys_menu` VALUES ('1236201540465778689', '0', 'Home', 100, 10, 'home', '', 'dianbiao', 1, 0, 0, '', 'system', '', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('1246597572403957761', '2019040915563913037130', '附件管理', 265, 20, 'sysattach', 'system/attach/index', 'skill', 1, 0, 0, '', 'system', '', 1, 0, 6);
INSERT INTO `sys_menu` VALUES ('1246597572496232450', '1246597572403957761', '查看', 10, 30, NULL, '', '', 1, 0, 0, 'system:attach:select', 'system', NULL, 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1246597572525592578', '1246597572403957761', '编辑', 20, 30, NULL, '', '', 1, 0, 0, 'system:attach:edit', 'system', NULL, 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1246597572538175490', '1246597572403957761', '删除', 30, 30, NULL, '', '', 1, 0, 0, 'system:attach:delete', 'system', NULL, 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1261465855938568193', '2019040915563913037130', '部门管理', 60, 20, 'sysdept', 'system/dept/index', 'theme', 1, 0, 0, '', 'system', NULL, 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('1261465856156672002', '1261465855938568193', '查看', 10, 30, NULL, NULL, NULL, 1, 0, 0, 'system:dept:select', 'system', NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1261465856186032129', '1261465855938568193', '编辑', 20, 30, NULL, NULL, NULL, 1, 0, 0, 'system:dept:edit', 'system', NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1261465856467050498', '1261465855938568193', '删除', 30, 30, NULL, NULL, NULL, 1, 0, 0, 'system:dept:delete', 'system', NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1261697492924239873', '2019040915563913037130', '岗位管理', 60, 20, 'syspost', 'system/post/index', 'el-icon-apple', 1, 0, 0, '', 'system', NULL, 1, 1, 2);
INSERT INTO `sys_menu` VALUES ('1261697493142343681', '1261697492924239873', '查看', 10, 30, NULL, NULL, NULL, 1, 0, 0, 'system:post:select', 'system', NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('1261697493247201281', '1261697492924239873', '编辑', 20, 30, NULL, NULL, NULL, 1, 0, 0, 'system:post:edit', 'system', NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('1261697493305921538', '1261697492924239873', '删除', 30, 30, NULL, NULL, NULL, 1, 0, 0, 'system:post:delete', 'system', NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('1265816471035998209', '2019040915563913037130', '调度日志', 60, 20, 'sysjoblog', 'system/joblog/index', 'question', 1, 0, 0, '', 'system', NULL, 0, 0, 4);
INSERT INTO `sys_menu` VALUES ('1265816471086329858', '1265816471035998209', '查看', 10, 30, NULL, NULL, NULL, 1, 0, 0, 'system:job:select', 'system', NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1283276986034667522', '2019040915563913037130', '邮件', 20, 20, 'sysemail', 'system/email/index', 'el-icon-truck', 1, 0, 0, '', 'system', NULL, 1, 1, 4);
INSERT INTO `sys_menu` VALUES ('1283276986084999170', '1283276986034667522', '查看', 10, 30, NULL, NULL, NULL, 1, 0, 0, 'system:mail:select', 'system', NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('1283276986105970690', '1283276986034667522', '编辑', 20, 30, NULL, NULL, NULL, 1, 0, 0, 'system:mail:edit', 'system', NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('1283276986122747906', '1283276986034667522', '删除', 30, 30, NULL, NULL, NULL, 1, 0, 0, 'system:mail:delete', 'system', NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('1284025836168941570', '1236201540465778689', '系统信息', 10, 20, 'sysinfo', 'home/sysinfo/index', 'online', 1, 0, 0, '', 'system', NULL, 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('1289588151459737601', '2019082319514900567416', '数据权限', 60, 30, NULL, '', '', 1, 0, 0, 'system:role:datascope', 'system', NULL, 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1396001466954633218', '0', '12', 2, 10, 'aaa', NULL, 'question', 1, 0, 1, NULL, NULL, NULL, 0, 1, 3);
INSERT INTO `sys_menu` VALUES ('1397789745883938817', '2019040916024028763683', '查看字典数据', 340, 30, NULL, NULL, NULL, 1, 1, 1, 'system:dict:select', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1397789852557672450', '2019040916024028763683', '修改字典数据', 350, 30, NULL, NULL, NULL, 1, 1, 1, 'system:dict:edit', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1397789972103725058', '2019040916024028763683', '删除字典数据', 360, 30, NULL, NULL, NULL, 1, 1, 1, 'system:dict:delete', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398485739355865089', '1174126024312606722', '编辑', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:menu:edit', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398485913981517825', '1261465855938568193', '新增', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:dept:add', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398486345948692482', '2019082319514900567416', '新增', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:role:add', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398486431386664962', '2019040916030120163963', '新增', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:param:add', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398486539796840449', '2019040916024028763683', '新增', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:dicttype:add', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398486594150825985', '2019040916024028763683', '新增', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:dict:add', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398486655307972610', '2019040916000705483768', '新增', 1, 30, NULL, NULL, NULL, 1, 1, 1, 'system:user:add', NULL, NULL, 1, 0, 1);
INSERT INTO `sys_menu` VALUES ('1398831831444398081', '2019040915563913037130', '文件管理', 1, 20, 'sysfiles', 'system/files/index', 'dakai', 1, 0, 1, 'system:files:manager', NULL, NULL, 1, 0, 2);
INSERT INTO `sys_menu` VALUES ('1399633791560507393', '2019040915563913037130', '文件2', 1, 20, 'sysfiles2', 'system/files/card', 'shuibiao', 1, 1, 1, NULL, NULL, NULL, 1, 1, 1);
INSERT INTO `sys_menu` VALUES ('2019040915563913037130', '0', '系统管理', 110, 10, 'system', '', 'tree', 1, 0, 0, '', 'system', '系统管理----这是一个顶级菜单', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('2019040916000705483768', '2019040915563913037130', '用户管理', 220, 20, 'sysuser', 'system/user/index', 'peoples', 1, 0, 0, '', 'system', '', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('2019040916024028763683', '2019040915563913037130', '数据字典', 250, 20, 'sysdict', 'system/dict/index', 'documentation', 1, 0, 0, '', 'system', '', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('2019040916030120163963', '2019040915563913037130', '参数设置', 260, 20, 'param', 'system/param/index', 'chatou', 1, 0, 0, '', 'system', '', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('2019081420522352867520', '2019040915563913037130', '区域管理', 310, 20, 'sysarea', 'system/area/index', 'quxiaolianjie', 1, 0, 0, '', 'system', '', 1, 0, 3);
INSERT INTO `sys_menu` VALUES ('2019082319514900567416', '2019040915563913037130', '角色管理', 230, 20, 'sysrole', 'system/role/index', 'shipinshixiao', 1, 0, 0, '', 'system', '', 1, 0, 3);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告ID',
  `read_report_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否需要已读回执',
  `send_type` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NOTICE' COMMENT '发送方式 NOTICE SMS EMAIL',
  `notice_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公告标题',
  `notice_type` int(0) UNSIGNED NOT NULL DEFAULT 10 COMMENT '公告类型（10通知 20公告）',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '公告内容',
  `status` int(0) NULL DEFAULT 0 COMMENT '公告状态（0正常 1关闭 2草稿）',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `top_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否置顶',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES ('1268412995613016066', 0, 'NOTICE', 'html测试', 20, '<p style=\"text-align:justify\"><span style=\"background-color:#ffffff; color:red; font-family:Tahoma,Helvetica,&quot;Microsoft Yahei&quot;,微软雅黑,Arial,STHeiti; font-size:12px\">&nbsp;common-fileupload.jar 包存在 ddos 漏洞，请尽快升级到最新的版本，直达链接：</span><a href=\"https://commons.apache.org/proper/commons-fileupload/\" style=\"background-color: rgb(255, 255, 255); font-family: Tahoma, Helvetica, &quot;Microsoft Yahei&quot;, 微软雅黑, Arial, STHeiti; font-size: 12px; text-decoration-line: none; outline: none;\">Apache fileupload jar<img alt=\"\" class=\"smiley\" src=\"http://127.0.0.1:8088/a/assets/module/ckeditor/plugins/smiley/images/a_030.png\" style=\"height:28px; width:28px\" title=\"\" /><img alt=\"\" class=\"smiley\" src=\"http://127.0.0.1:8088/a/assets/module/ckeditor/plugins/smiley/images/a_047.gif\" style=\"height:28px; width:28px\" title=\"\" /><img alt=\"\" class=\"smiley\" src=\"http://127.0.0.1:8088/a/assets/module/ckeditor/plugins/smiley/images/a_035.png\" style=\"float:right; height:28px; width:28px\" title=\"\" /></a></p>\n\n<div style=\"margin-left:5px; margin-right:5px\">\n<p>UEditor 所提供的所有后端代码都仅为 DEMO 作用，<strong>切不可直接使用到生产环境中</strong>，目前已知 php 的代码会存在 ssrf 的安全漏洞。修复方式：使用最新的 Uploader&nbsp;<a href=\"https://github.com/fex-team/ueditor/blob/dev-1.5.0/php/Uploader.class.php\" style=\"outline: none;\">https://github.com/fex-team/ueditor/blob/dev-1.5.0/php/Uploader.class.php</a></p>\n\n<p>因该问题导致的安全漏洞，与产品本身无关。</p>\n</div>\n\n<p style=\"margin-left:5px; margin-right:5px\">注：线上演示版上传功能只作为功能演示，1.4.3以上版本将不再承诺支持ie6/ie7。<span style=\"color:#1aa304\"><a href=\"http://word.baidu.com/\" style=\"text-decoration-line: none; outline: none; color: red;\">百度doc</a>&nbsp;测试版已上线，欢迎试用和反馈!</span></p>\n', 1, '2020-06-04 13:27:19', '2020-06-04 13:27:21', 1, 'super', '2020-06-04 13:23:19', 'super', '2020-06-28 18:40:22', '', 0);
INSERT INTO `sys_notice` VALUES ('1268413056497532930', 0, 'NOTICE', 'Fastjson 漏洞修复', 20, '<p>本系统修复。。。阿斯顿发a<span style=\"background-color:#2ecc71\">d萨法撒的发生</span>大<span style=\"font-size:22px\"><span style=\"color:#e74c3c\">发发大水发放阿萨大发大阿萨</span></span>达发送到发送到发放</p>\n\n<p>&nbsp;</p>\n', 1, '2020-06-04 13:27:29', '2020-06-04 13:27:32', 0, 'super', '2020-06-04 13:23:33', 'super', '2020-06-28 18:35:18', '423\n', 0);

-- ----------------------------
-- Table structure for sys_online_msg
-- ----------------------------
DROP TABLE IF EXISTS `sys_online_msg`;
CREATE TABLE `sys_online_msg`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `sender_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送者id',
  `sender_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送者账号',
  `receiver_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收者id',
  `receiver_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收者账号',
  `read_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '已读标志 0未读 1已读',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `router` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面路由',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除：1，是；0，否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '在线用户消息记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `param_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `param_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名称key',
  `param_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值value',
  `param_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数描述',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `module` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '模块',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `post_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上级岗位id',
  `parent_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上级岗位code',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '岗位名称',
  `post_sort` int(0) NULL DEFAULT NULL COMMENT '排序号',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `stop_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：0正常  1停用',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记：0正常 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `role_sort` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '角色排序',
  `status` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：1正常  0停用',
  `del_flag` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '删除状态 0正常  1删除 ',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  `data_scope_type` int(0) UNSIGNED NULL DEFAULT 20 COMMENT '数据范围类型：10全部数据权限 20本部门及以下数据权限 30本部门数据权限 40自定数据权限 ',
  `create_dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '角色id',
  `dept_id` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '部门id',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '角色部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色表主键',
  `menu_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单表主键',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所在部门',
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` int(0) NULL DEFAULT NULL COMMENT '性别：1，男；2，女',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `user_type` int(0) NULL DEFAULT NULL COMMENT '用户类型',
  `sort` int(0) NULL DEFAULT NULL COMMENT '用户排序',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `login_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登陆IP',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登陆时间',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '账号状态：1正常 0锁定',
  `source` tinyint(0) UNSIGNED NULL DEFAULT 1 COMMENT '1系统录入 2qq登录 3微信登录 4微博登录 5支付宝登录',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除：1，是；0，否',
  `version` int(0) UNSIGNED NULL DEFAULT 1 COMMENT '乐观锁',
  `create_dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_account`(`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('super', '1000', 'super', '$2a$10$Dh4yWRuiQ8fI.UFSE7.k.O5ws8jZHPctabYBkrDLQIyXEaD2ztWOy', '超级管理', '哦呦', 1, '222@qq.com', '16601110111', 2, 10, 'http://127.0.0.1:8070/files/20210601/0a2d37dca8b444e68691573a4d3ddcb4.', '127.0.0.1', '2020-05-06 22:55:52', 1, 1, '2', '2', '2020-05-16 14:41:30', 'super', '2021-06-01 16:42:35', 0, 1, '1000');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统用户表主键',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_gen
-- ----------------------------
DROP TABLE IF EXISTS `test_gen`;
CREATE TABLE `test_gen`  (
  `test_id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `test_label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `del_flag` tinyint(1) NULL DEFAULT NULL,
  `version` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`test_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_gen
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
