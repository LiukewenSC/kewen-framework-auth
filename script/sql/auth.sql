/*
 Navicat Premium Data Transfer

 Source Server         : Aliyun
 Source Server Type    : MySQL
 Source Server Version : 80028 (8.0.28)
 Source Host           : liukewensc.mysql.rds.aliyuncs.com:3306
 Source Schema         : kewen_framework_auth_template

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 05/07/2024 11:00:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_auth_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_data`;
CREATE TABLE `sys_auth_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模块',
  `operate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'unified' COMMENT '操作类型 unified modify delete 等,应用可以自定义操作名称',
  `data_id` bigint NOT NULL COMMENT '数据ID 应用中业务的主键ID',
  `authority` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限字符串',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `business_id`(`data_id` ASC, `module` ASC, `operate` ASC, `authority` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_auth_data
-- ----------------------------
INSERT INTO `sys_auth_data` VALUES (1, 'testrange', 'unified', 1, 'ROLE_1', 'ROLE_超级管理员', '2024-07-04 14:27:11', '2024-07-04 16:06:36');
INSERT INTO `sys_auth_data` VALUES (2, 'testedit', 'unified', 1, 'ROLE_1', 'ROLE_超级管理员', '2024-07-04 14:27:11', '2024-07-04 14:27:11');
INSERT INTO `sys_auth_data` VALUES (3, 'testauthedit', 'unified', 1, 'ROLE_1', 'ROLE_超级管理员', '2024-07-04 14:27:11', '2024-07-04 14:27:11');

-- ----------------------------
-- Table structure for sys_auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_menu`;
CREATE TABLE `sys_auth_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `authority` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限字符串',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_auth_menu
-- ----------------------------
INSERT INTO `sys_auth_menu` VALUES (1, 1, 'ROLE_1', 'ROLE_超级管理员', '2024-05-11 16:59:12', '2024-05-11 17:00:40');
INSERT INTO `sys_auth_menu` VALUES (4, 4, 'ROLE_1', 'ROLE_超级管理员', '2024-06-14 16:41:11', '2024-06-14 16:41:11');
INSERT INTO `sys_auth_menu` VALUES (6, 9, 'ROLE_1', 'ROLE_超级管理员', '2024-06-14 16:41:11', '2024-06-14 16:41:11');
INSERT INTO `sys_auth_menu` VALUES (7, 101, 'ROLE_1', 'ROLE_超级管理员', '2024-06-14 16:41:11', '2024-06-14 16:41:11');
INSERT INTO `sys_auth_menu` VALUES (8, 133, 'ROLE_1', 'ROLE_超级管理员', '2024-06-14 16:41:11', '2024-06-14 16:41:11');
INSERT INTO `sys_auth_menu` VALUES (9, 11, 'ROLE_1', 'ROLE_超级管理员', '2024-06-19 08:46:39', '2024-06-19 08:46:43');
INSERT INTO `sys_auth_menu` VALUES (18, 3, 'ROLE_2', 'ROLE_管理员', '2024-06-19 17:58:05', '2024-06-19 17:58:05');
INSERT INTO `sys_auth_menu` VALUES (19, 3, 'ROLE_1', 'ROLE_超级管理员', '2024-06-19 17:58:05', '2024-06-19 17:58:05');
INSERT INTO `sys_auth_menu` VALUES (20, 5, 'ROLE_1', 'ROLE_超级管理员', '2024-06-19 18:01:37', '2024-06-19 18:01:37');
INSERT INTO `sys_auth_menu` VALUES (21, 2, 'DEPT_2', 'DEPT_总部', '2024-06-20 00:05:38', '2024-06-20 00:05:38');
INSERT INTO `sys_auth_menu` VALUES (22, 2, 'ROLE_1', 'ROLE_超级管理员', '2024-06-20 00:05:38', '2024-06-20 00:05:38');
INSERT INTO `sys_auth_menu` VALUES (23, 2, 'DEPT_1', 'DEPT_根部门', '2024-06-20 00:05:38', '2024-06-20 00:05:38');
INSERT INTO `sys_auth_menu` VALUES (24, 8, 'ROLE_1', 'ROLE_超级管理员', '2024-06-20 00:27:40', '2024-06-20 00:27:40');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名',
  `parent_id` bigint NOT NULL COMMENT '部门，如果部门为0 则代表根部门',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '根部门', 0, '2024-05-11 16:37:23', '2024-05-11 16:37:35');
INSERT INTO `sys_dept` VALUES (2, '总部', 0, '2024-06-20 00:03:08', '2024-06-20 00:03:08');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父id',
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接或路径',
  `redirect` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向路由',
  `component` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名',
  `meta` json NULL COMMENT '元信息',
  `icon` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `hidden` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏菜单，隐藏了则只有路由加载，不在菜单列表加载',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '类型： 1-菜单 2-按钮 3-外部链接',
  `auth_type` tinyint NOT NULL DEFAULT 1 COMMENT '权限类型 1-基于父菜单权限 2-基于本身权限',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除，默认0-未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2, '首页', 0, '/home', NULL, 'home', '{\"affix\": true, \"externalLinkUrl\": null, \"isExternalLinks\": false}', 'el-icon-s-home', 0, 1, 2, NULL, '2024-05-10 14:56:39', '2024-06-18 08:01:55', 0);
INSERT INTO `sys_menu` VALUES (3, '文档', 0, '/docs', NULL, 'docs', NULL, 'el-icon-s-order', 0, 1, 2, NULL, '2024-05-10 14:56:39', '2024-06-18 08:01:34', 0);
INSERT INTO `sys_menu` VALUES (4, '组织机构管理', 0, '/organization', '/organization/user', 'layout/publics', NULL, 'el-icon-s-help', 0, 1, 2, NULL, '2024-05-10 14:56:39', '2024-06-21 08:34:49', 0);
INSERT INTO `sys_menu` VALUES (5, '部门管理', 4, '/organization/dept', NULL, 'organization/dept', NULL, 'el-icon-s-platform', 0, 1, 1, NULL, '2024-05-10 14:56:39', '2024-06-21 08:34:29', 0);
INSERT INTO `sys_menu` VALUES (6, '用户管理', 4, '/organization/user', NULL, 'organization/User', NULL, 'el-icon-s-platform', 0, 1, 1, NULL, '2024-05-10 14:56:39', '2024-06-24 16:47:04', 0);
INSERT INTO `sys_menu` VALUES (7, '角色管理', 4, '/organization/role', NULL, 'organizaion/role', NULL, 'el-icon-s-custom', 0, 1, 1, NULL, '2024-05-10 14:56:39', '2024-06-21 08:34:41', 0);
INSERT INTO `sys_menu` VALUES (8, '个人中心', 0, '/profile', NULL, 'profile', '{\"hidden\": true}', 'el-icon-star-on', 1, 1, 2, NULL, '2024-05-10 14:56:39', '2024-06-14 16:45:17', 0);
INSERT INTO `sys_menu` VALUES (9, '路由嵌套', 0, '/route', '/menu', 'layout/publics', NULL, 'el-icon-s-data', 0, 1, 2, NULL, '2024-05-10 14:56:39', '2024-06-19 08:37:28', 0);
INSERT INTO `sys_menu` VALUES (10, '菜单管理', 9, '/menu', NULL, 'menu', NULL, 'el-icon-s-operation', 0, 1, 1, NULL, '2024-05-10 14:56:39', '2024-06-19 08:34:48', 0);
INSERT INTO `sys_menu` VALUES (11, '菜单管理2', 0, '/menu2', NULL, 'menu', NULL, 'el-icon-s-operation', 0, 1, 2, NULL, '2024-05-10 14:56:39', '2024-06-19 08:46:15', 0);
INSERT INTO `sys_menu` VALUES (101, '组件模板', 0, '/example', NULL, 'example', NULL, 'el-icon-s-platform', 0, 1, 2, NULL, '2024-06-14 15:57:52', '2024-06-14 16:45:17', 0);
INSERT INTO `sys_menu` VALUES (117, '组件例子', 101, '/demo', '/demo/filtering', 'layout/publics', NULL, 'el-icon-star-on', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (118, '筛选组件', 117, '/demo/filtering', NULL, 'demo/filtering/index', NULL, 'el-icon-s-marketing', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (119, '筛选组件详情', 117, '/demo/filtering-details', NULL, 'demo/filtering/component/details', '{\"hidden\": true}', 'el-icon-s-marketing', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (120, 'v-charts 图表', 117, '/demo/v-charts', NULL, 'demo/vCharts/index', NULL, 'el-icon-data-analysis', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (121, '大数据可视化', 117, 'externalLinkUrl', NULL, NULL, '{\"externalLinkUrl\": \"/big-data\", \"isExternalLinks\": true}', 'el-icon-s-data', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (122, '仿电视开关机', 117, '/demo/tVSwitch', NULL, 'demo/tVSwitch/index', NULL, 'el-icon-s-platform', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (123, '富文本编辑器', 117, '/demo/mavonEditor', NULL, 'demo/mavonEditor/index', NULL, 'el-icon-edit-outline', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (124, '瀑布屏', 117, '/demo/waterfall', NULL, 'demo/waterfall/index', NULL, 'el-icon-reading', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (125, '论坛评价', 117, '/demo/comment', NULL, 'demo/comment/index', NULL, 'el-icon-chat-line-square', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (126, 'Tree 树形控件', 117, '/demo/treeControl', NULL, 'demo/treeControl/index', NULL, 'el-icon-finished', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (127, '天地图', 117, '/demo/tianditu', NULL, 'demo/tianditu/index', NULL, 'el-icon-location-information', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (128, '表格', 117, '/demo/table', NULL, 'demo/table/index', NULL, 'el-icon-tickets', 0, 1, 1, NULL, '2024-05-10 14:56:40', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (129, '表单页', 101, '/forms', '/forms/basic-form', 'layout/publics', NULL, 'el-icon-notebook-2', 0, 1, 1, NULL, '2024-05-10 14:56:41', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (130, '基础表单', 129, '/forms/basic-form', NULL, 'forms/basicForm/index', NULL, 'el-icon-cloudy', 0, 1, 1, NULL, '2024-05-10 14:56:41', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (131, '分步表单', 129, '/forms/step-form', NULL, 'forms/stepForm/index', NULL, 'el-icon-partly-cloudy', 0, 1, 1, NULL, '2024-05-10 14:56:41', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (132, '高级表单', 129, '/forms/advanced-form', NULL, 'forms/advancedForm/index', NULL, 'el-icon-cloudy-and-sunny', 0, 1, 1, NULL, '2024-05-10 14:56:41', '2024-06-14 15:55:57', 0);
INSERT INTO `sys_menu` VALUES (133, '工具类集合', 0, '/tools', NULL, 'tools', NULL, 'el-icon-setting', 0, 1, 2, NULL, '2024-05-10 14:56:41', '2024-06-14 16:45:17', 0);
INSERT INTO `sys_menu` VALUES (134, '外链', 101, 'externalLinkUrl', NULL, NULL, '{\"externalLinkUrl\": \"https://www.baidu.com\", \"isExternalLinks\": true, \"externalLinkType\": \"open\"}', 'el-icon-link', 0, 1, 1, NULL, '2024-05-10 14:56:41', '2024-06-14 15:55:57', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '2024-05-11 16:38:22', '2024-05-11 16:38:22');
INSERT INTO `sys_role` VALUES (2, '管理员', '2024-06-19 17:57:06', '2024-06-19 17:57:06');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar_file_id` bigint NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint NOT NULL DEFAULT 1 COMMENT '1-男 2-女 3-..',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '超级管理员', '超级管理员', 'admin', '12110', '593655063@qq.com', NULL, 1, '2024-05-11 11:26:05', '2024-06-20 17:24:58');
INSERT INTO `sys_user` VALUES (2, '管理员', '管理', 'admin2', NULL, NULL, NULL, 2, '2024-06-20 17:25:13', '2024-06-20 23:36:32');
INSERT INTO `sys_user` VALUES (4, 'a', 'a', 'a', 'a', 'b', NULL, 1, '2024-06-25 16:17:38', '2024-06-25 16:17:47');

-- ----------------------------
-- Table structure for sys_user_credential
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_credential`;
CREATE TABLE `sys_user_credential`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户表的id',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码,可以为空，为拓展免密登录做准备',
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `password_expired_time` datetime NULL DEFAULT NULL COMMENT '凭证过期时间 每次修改密码应修改过期时间 ， 为空表示系统无过期时间设定',
  `account_locked_deadline` datetime NULL DEFAULT NULL COMMENT '账号锁定截止时间，为空或早于当前时间则为不锁定',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '账号是否启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户凭证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_credential
-- ----------------------------
INSERT INTO `sys_user_credential` VALUES (1, 1, '$2a$10$G74t5HhUl1O1vNIDnSnfpeMGvhM3hM5IskPX1Mlt.OIUN2s9x63LS', '123456', NULL, NULL, 1, '2024-05-11 11:26:26', '2024-07-02 12:51:13');

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `is_primary` tinyint NOT NULL DEFAULT 0 COMMENT '是否主要归属部门 0-非主要部门 1-主要部门',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC, `dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (1, 1, 1, 1, '2024-05-11 16:37:50', '2024-05-11 16:37:50');

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户账号',
  `github_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'github账号',
  `gitee_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'gitee账号',
  `wechat_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信账号',
  `profession` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职业',
  `introduction` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `resume` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '个人简历',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2024-05-11 16:38:11', '2024-05-11 16:38:11');

-- ----------------------------
-- Table structure for testauth_annotation_business
-- ----------------------------
DROP TABLE IF EXISTS `testauth_annotation_business`;
CREATE TABLE `testauth_annotation_business`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of testauth_annotation_business
-- ----------------------------
INSERT INTO `testauth_annotation_business` VALUES (1, 'test1', 'test1description', '2024-07-04 14:25:08', '2024-07-04 14:25:11', 0);
INSERT INTO `testauth_annotation_business` VALUES (2, 't2', 'test2', NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
