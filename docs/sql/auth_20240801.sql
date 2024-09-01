/*
 Navicat Premium Data Transfer

 Source Server         : Aliyun
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : liukewensc.mysql.rds.aliyuncs.com:3306
 Source Schema         : kewen_framework_auth_template

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 20/08/2024
*/

SET NAMES utf8mb4;

-- ----------------------------
-- Table structure for sys_auth_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_data`;
CREATE TABLE `sys_auth_data`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `business_function` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务功能',
    `data_id` bigint NOT NULL COMMENT '数据ID 应用中业务的主键ID',
    `operate` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'unified' COMMENT '操作类型 unified modify delete 等,应用可以自定义操作名称',
    `authority` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限字符串',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限描述',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `business_function`(`business_function` ASC, `operate` ASC, `data_id` ASC, `authority` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_menu`;
CREATE TABLE `sys_auth_menu`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `api_id` bigint NOT NULL COMMENT '菜单id',
    `authority` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限字符串',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限描述',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_api`;
CREATE TABLE `sys_menu_api`  (
    `id` bigint NOT NULL COMMENT '主键id',
    `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求路径',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求名称',
    `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父id',
    `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
    `auth_type` tinyint NOT NULL DEFAULT 1 COMMENT '权限类型 1-基于自身权限 2-基于父权限',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除，默认0-未删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_sys_menu_api_path`(`path` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_route`;
CREATE TABLE `sys_menu_route`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名',
    `parent_id` bigint NULL DEFAULT NULL COMMENT '父路由id',
    `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接或路径',
    `redirect` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向路由',
    `component` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名',
    `meta` json NULL COMMENT '元信息',
    `icon` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
    `hidden` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏菜单，隐藏了则只有路由加载，不在菜单列表加载',
    `api_id` bigint NULL DEFAULT NULL COMMENT '请求地址id',
    `type` tinyint NOT NULL DEFAULT 1 COMMENT '类型： 1-菜单 2-按钮 3-外部链接',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除，默认0-未删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户凭证表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户部门关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;



-- 创建便于查看API菜单权限的视图，只查询以自身权限为主的
create view view_menu_api_auth as
select i.id,i.path,i.name,i.parent_id,m.authority,m.description
from sys_menu_api i left join sys_auth_menu m on i.id=m.api_id
where i.deleted =0 and i.auth_type=1
order by i.path,m.authority