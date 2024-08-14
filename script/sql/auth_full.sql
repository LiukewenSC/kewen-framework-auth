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
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
    `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_auth_menu
-- ----------------------------
INSERT INTO `sys_auth_menu` VALUES (1, 1, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (2, 1818496367898750976, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (3, 1818496367898750979, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (4, 1818496367898750985, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (5, 1818496367898750991, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (6, 1818496367898750997, 'ROLE_2', 'ROLE_管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (7, 1818496367898750999, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (8, 1818496367898750998, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (9, 1820708571775561728, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_auth_menu` VALUES (10, 1823284778685722624, 'ROLE_1', 'ROLE_超级管理员', '2024-08-01 00:00:00', NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名',
    `parent_id` bigint NOT NULL COMMENT '部门，如果部门为0 则代表根部门',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '根部门', 0, '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_dept` VALUES (2, '总部', 0, '2024-08-01 00:00:00', NULL);

-- ----------------------------
-- Table structure for sys_menu_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_api`;
CREATE TABLE `sys_menu_api`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
     `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求路径',
     `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求名称',
     `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父id',
     `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
     `auth_type` tinyint NOT NULL DEFAULT 1 COMMENT '权限类型 1-基于自身权限 2-基于父权限',
     `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
     `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除，默认0-未删除',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1823284778685722629 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu_api
-- ----------------------------
INSERT INTO `sys_menu_api` VALUES (1818496367898750976, '/menu/api', '菜单路径相关接口', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750977, '/menu/api/tree', '菜单路径相关接口>menuTree', 1818496367898750976, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750978, '/menu/api/update', '菜单路径相关接口>updateMenu', 1818496367898750976, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750979, '/rabc/dept', 'RabcDeptController', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750980, '/rabc/dept/add', 'RabcDeptController>添加部门', 1818496367898750979, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750981, '/rabc/dept/delete', 'RabcDeptController>删除部门', 1818496367898750979, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750982, '/rabc/dept/list', 'RabcDeptController>部门列表', 1818496367898750979, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750983, '/rabc/dept/page', 'RabcDeptController>部门分页', 1818496367898750979, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750984, '/rabc/dept/update', 'RabcDeptController>部门列表', 1818496367898750979, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750985, '/rabc/role', 'RabcRoleController', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750986, '/rabc/role/add', 'RabcRoleController>添加角色', 1818496367898750985, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750987, '/rabc/role/delete', 'RabcRoleController>删除角色', 1818496367898750985, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750988, '/rabc/role/list', 'RabcRoleController>角色列表', 1818496367898750985, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750989, '/rabc/role/page', 'RabcRoleController>角色分页', 1818496367898750985, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750990, '/rabc/role/update', 'RabcRoleController>修改角色', 1818496367898750985, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750991, '/rabc/user', '用户相关', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750992, '/rabc/user/add', '用户相关>新增', 1818496367898750991, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750993, '/rabc/user/delete', '用户相关>delete', 1818496367898750991, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750994, '/rabc/user/list', '用户相关>用户列表', 1818496367898750991, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750995, '/rabc/user/page', '用户相关>分页', 1818496367898750991, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750996, '/rabc/user/update', '用户相关>update', 1818496367898750991, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750997, '/test', 'TestAuthAnnotationController', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750998, '/test/checkMenu', 'TestAuthAnnotationController>测试菜单控制', 1818496367898750997, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898750999, '/testAnnoClassMenuController', '测试只在Controller上加@AuthMenu权限注解', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751000, '/testAnnoClassMenuController/hello', '测试只在Controller上加@AuthMenu权限注解>hello', 1818496367898750999, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751001, '/testAnnoClassMenuController/hello2', '测试只在Controller上加@AuthMenu权限注解>hello2', 1818496367898750999, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751002, '/testAuthMenuClassMethodController', '测试在Method和Controller上加注解权限', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751003, '/testAuthMenuClassMethodController/hello', '测试在Method和Controller上加注解权限>你好1', 1818496367898751002, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751004, '/testAuthMenuClassMethodController/hello2', '测试在Method和Controller上加注解权限>你好2', 1818496367898751002, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751005, '/testAuthMenuClassMethodController/hello3', '测试在Method和Controller上加注解权限>你好3', 1818496367898751002, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751006, '/testAuthMenuMethod', 'TestAuthMenuMethodController>测试只在Method上加@AuthMenu权限注解', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818496367898751007, '/testAuthMenuMethod2', 'TestAuthMenuMethodController2>测试只在Method上加@AuthMenu权限注解2，Controller上没有注解', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1818547777143074816, '/test/checkMenuNone', 'TestAuthAnnotationController>测试菜单控制', 1818496367898750997, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1823284778685722624, '/meetingRoomPrimary', 'MeetingRoomPrimaryController', 0, NULL, 1, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1823284778685722625, '/meetingRoomPrimary/add', 'MeetingRoomPrimaryController>创建会议室', 1823284778685722624, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1823284778685722626, '/meetingRoomPrimary/add2', 'MeetingRoomPrimaryController>创建会议室', 1823284778685722624, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1823284778685722627, '/meetingRoomPrimary/deleteById', 'MeetingRoomPrimaryController>删除会议室', 1823284778685722624, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1823284778685722628, '/meetingRoomPrimary/updatePrimaryAuth', 'MeetingRoomPrimaryController>编辑会议室主权限', 1823284778685722624, NULL, 2, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_api` VALUES (1823557356394409984, '/menu/route', '菜单路由相关接口', 0, NULL, 1, '2024-08-14 11:08:48', '2024-08-14 11:08:48', 0);
INSERT INTO `sys_menu_api` VALUES (1823557356394409985, '/menu/route/tree', '菜单路由相关接口>routeTrees', 1823557356394409984, NULL, 2, '2024-08-14 11:08:48', '2024-08-14 11:08:48', 0);

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
    `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除，默认0-未删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu_route
-- ----------------------------
INSERT INTO `sys_menu_route` VALUES (2, '首页', 0, '/home', NULL, 'home', '{\"affix\": true, \"externalLinkUrl\": null, \"isExternalLinks\": false}', 'el-icon-s-home', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (3, '文档', 0, '/docs', NULL, 'docs', NULL, 'el-icon-s-order', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (4, '组织机构管理', 0, '/organization', '/organization/user', 'layout/publics', NULL, 'el-icon-s-help', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (5, '部门管理', 4, '/organization/dept', NULL, 'organization/Dept', NULL, 'el-icon-s-platform', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (6, '用户管理', 4, '/organization/user', NULL, 'organization/User', NULL, 'el-icon-s-platform', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (7, '角色管理', 4, '/organization/role', NULL, 'organization/Role', NULL, 'el-icon-s-custom', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (8, '个人中心', 0, '/profile', NULL, 'profile', '{\"hidden\": true}', 'el-icon-star-on', 1, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (9, '菜单路由', 0, '/route', '/menu', 'layout/publics', NULL, 'el-icon-s-data', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (10, '菜单管理', 9, '/menu', NULL, 'menu', NULL, 'el-icon-s-operation', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (11, '权限框架测试', 0, '/test', '/test/datarange', 'layout/publics', NULL, 'el-icon-tickets', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (12, '@DataRange权限范围', 11, '/test/datarange', NULL, 'test/DataRange', NULL, 'el-icon-tickets', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (13, '@AuthMenu权限列表', 11, '/test/authmenu', NULL, 'test/AuthMenu', NULL, 'el-icon-tickets', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (14, '@AuthCheckDataOperation数据编辑', 11, '/test/authdataoperation', NULL, 'test/AuthCheckDataOperation', NULL, 'el-icon-tickets', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (101, '组件模板', 0, '/example', NULL, 'example', NULL, 'el-icon-s-platform', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (117, '组件例子', 101, '/demo', '/demo/filtering', 'layout/publics', NULL, 'el-icon-star-on', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (118, '筛选组件', 117, '/demo/filtering', NULL, 'demo/filtering/index', NULL, 'el-icon-s-marketing', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (119, '筛选组件详情', 117, '/demo/filtering-details', NULL, 'demo/filtering/component/details', '{\"hidden\": true}', 'el-icon-s-marketing', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (120, 'v-charts 图表', 117, '/demo/v-charts', NULL, 'demo/vCharts/index', NULL, 'el-icon-data-analysis', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (121, '大数据可视化', 117, 'externalLinkUrl', NULL, 'demo/bigData', '{\"externalLinkUrl\": \"/big-data\", \"isExternalLinks\": true}', 'el-icon-s-data', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (122, '仿电视开关机', 117, '/demo/tVSwitch', NULL, 'demo/tVSwitch/index', NULL, 'el-icon-s-platform', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (123, '富文本编辑器', 117, '/demo/mavonEditor', NULL, 'demo/mavonEditor/index', NULL, 'el-icon-edit-outline', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (124, '瀑布屏', 117, '/demo/waterfall', NULL, 'demo/waterfall/index', NULL, 'el-icon-reading', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (125, '论坛评价', 117, '/demo/comment', NULL, 'demo/comment/index', NULL, 'el-icon-chat-line-square', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (126, 'Tree 树形控件', 117, '/demo/treeControl', NULL, 'demo/treeControl/index', NULL, 'el-icon-finished', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (127, '天地图', 117, '/demo/tianditu', NULL, 'demo/tianditu/index', NULL, 'el-icon-location-information', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (128, '表格', 117, '/demo/table', NULL, 'demo/table/index', NULL, 'el-icon-tickets', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (129, '表单页', 101, '/forms', '/forms/basic-form', 'layout/publics', NULL, 'el-icon-notebook-2', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (130, '基础表单', 129, '/forms/basic-form', NULL, 'forms/basicForm/index', NULL, 'el-icon-cloudy', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (131, '分步表单', 129, '/forms/step-form', NULL, 'forms/stepForm/index', NULL, 'el-icon-partly-cloudy', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (132, '高级表单', 129, '/forms/advanced-form', NULL, 'forms/advancedForm/index', NULL, 'el-icon-cloudy-and-sunny', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (133, '工具类集合', 0, '/tools', NULL, 'tools', NULL, 'el-icon-setting', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);
INSERT INTO `sys_menu_route` VALUES (134, '外链', 101, 'externalLinkUrl', NULL, NULL, '{\"externalLinkUrl\": \"https://www.baidu.com\", \"isExternalLinks\": true, \"externalLinkType\": \"open\"}', 'el-icon-link', 0, NULL, 1, NULL, '2024-08-01 00:00:00', NULL, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_role` VALUES (2, '管理员', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_role` VALUES (3, '普通角色', '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_role` VALUES (4, '会议室管理员', '2024-08-01 00:00:00', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '超级管理员', '超级管理员', 'admin', '12110', '593655063@qq.com', NULL, 1, '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_user` VALUES (2, '管理员', '管理', 'admin2', NULL, NULL, NULL, 2, '2024-08-01 00:00:00', NULL);
INSERT INTO `sys_user` VALUES (7, '测试MenuAuth', '测试MenuAuth', '测试MenuAuth', '6019521325', 'test@example.us', NULL, 2, '2024-08-01 00:00:00', NULL);

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
INSERT INTO `sys_user_credential` VALUES (1, 1, '{bcrypt}$2a$10$uCJpehGeWfvENTq.6LVrquPrGhZ9/QJXHmXmkNlu9cvnevFra1YdW', '123456', NULL, NULL, 1, '2024-08-01 00:00:00', NULL);

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
    `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id`(`user_id` ASC, `dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (1, 1, 1, 1, '2024-08-01 00:00:00', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id` bigint NOT NULL COMMENT '用户id',
    `role_id` bigint NOT NULL COMMENT '角色id',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2024-08-01 00:00:00', NULL);




-- 应用权限
CREATE TABLE sys_auth_data (
    id                bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    business_function varchar(64)  NOT NULL COMMENT '业务功能',
    data_id           bigint       NOT NULL COMMENT '数据ID 应用中业务的主键ID',
    operate           varchar(64)  NOT NULL DEFAULT 'unified' COMMENT '操作类型 unified modify delete 等,应用可以自定义操作名称',
    authority         varchar(100) NOT NULL COMMENT '权限字符串',
    description       varchar(200) NOT NULL COMMENT '权限描述',
    create_time       datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       datetime              DEFAULT null ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    KEY (business_function, operate, data_id, authority) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC COMMENT ='应用权限表';

SET FOREIGN_KEY_CHECKS = 1;
