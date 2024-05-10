
-- ----------------------------
-- Table structure for sys_menu_auth
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_menu`;
CREATE TABLE `sys_auth_menu`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `menu_id`     bigint       NOT NULL COMMENT '菜单id',
    `authority`   varchar(100) NOT NULL COMMENT '权限字符串',
    `description` varchar(200) NOT NULL COMMENT '权限描述',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_application_auth
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_data`;
CREATE TABLE `sys_auth_application`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `module`      varchar(64)  NOT NULL COMMENT '模块',
    `business_id` bigint       NOT NULL COMMENT '业务ID 应用中业务的主键ID',
    `operate`     varchar(64)  NOT NULL DEFAULT 'unified' COMMENT '操作类型 unified modify delete 等,应用可以自定义操作名称',
    `authority`   varchar(100) NOT NULL COMMENT '权限字符串',
    `description` varchar(200) NOT NULL COMMENT '权限描述',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `business_id`(`business_id` ASC, `module` ASC, `operate` ASC, `authority` ASC) USING BTREE
) ENGINE = InnoDB   COMMENT = '应用数据权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`           varchar(100) NULL DEFAULT NULL COMMENT '姓名',
    `nick_name`      varchar(100) NULL DEFAULT NULL COMMENT '用户昵称',
    `username`       varchar(100) NOT NULL COMMENT '用户名',
    `phone`          varchar(20) NULL DEFAULT NULL COMMENT '手机号',
    `email`          varchar(32) NULL DEFAULT NULL COMMENT '邮箱',
    `avatar_file_id` bigint NULL DEFAULT NULL COMMENT '头像',
    `gender`         tinyint      NOT NULL DEFAULT 1 COMMENT '1-男 2-女 3-..',
    `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username`(`username` ASC) USING BTREE,
    UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB   COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_credential
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_credential`;
CREATE TABLE `sys_user_credential`
(
    `id`                      bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`                 bigint   NOT NULL COMMENT '用户表的id',
    `password`                varchar(200) NULL DEFAULT NULL COMMENT '用户密码,可以为空，为拓展免密登录做准备',
    `remark`                  varchar(64) NULL DEFAULT NULL COMMENT '备注',
    `password_expired_time`   datetime NULL DEFAULT NULL COMMENT '凭证过期时间 每次修改密码应修改过期时间 ， 为空表示系统无过期时间设定',
    `account_locked_deadline` datetime NULL DEFAULT NULL COMMENT '账号锁定截止时间，为空或早于当前时间则为不锁定',
    `enabled`                 tinyint(1) NOT NULL DEFAULT 1 COMMENT '账号是否启用',
    `create_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB   COMMENT = '用户凭证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`
(
    `id`             bigint   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`        bigint   NOT NULL COMMENT '用户账号',
    `github_account` varchar(64) NULL DEFAULT NULL COMMENT 'github账号',
    `gitee_account`  varchar(64) NULL DEFAULT NULL COMMENT 'gitee账号',
    `wechat_account` varchar(64) NULL DEFAULT NULL COMMENT '微信账号',
    `profession`     varchar(128) NULL DEFAULT NULL COMMENT '职业',
    `introduction`   varchar(512) NULL DEFAULT NULL COMMENT '个人简介',
    `resume`         text NULL COMMENT '个人简历',
    `create_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `name`        varchar(100) NOT NULL COMMENT '部门名',
    `parent_id`   bigint       NOT NULL COMMENT '部门，如果部门为0 则代表根部门',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  COMMENT = '部门表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '角色id',
    `name`        varchar(100) NOT NULL COMMENT '角色名',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint NOT NULL COMMENT '用户id',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `user_id`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = DYNAMIC;
