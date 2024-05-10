-- 菜单表
CREATE TABLE `sys_menu`
(
    `id`          bigint  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单名',
    `parent_id`   bigint  NOT NULL                                              DEFAULT '0' COMMENT '父id',
    `path`        varchar(512)                                                  DEFAULT NULL COMMENT '链接或路径',
    `redirect`    varchar(64)                                                   DEFAULT NULL COMMENT '重定向路由',
    `component`   varchar(64)                                                   DEFAULT NULL COMMENT '组件名',
    `meta`        json                                                          DEFAULT NULL COMMENT '元信息',
    `icon`        varchar(512)                                                  DEFAULT NULL COMMENT '图片地址',
    `type`        tinyint NOT NULL                                              DEFAULT '1' COMMENT '类型： 1-菜单 2-按钮 3-外部链接',
    `auth_type`   tinyint NOT NULL                                              DEFAULT '1' COMMENT '权限类型 1-基于父菜单权限 2-基于本身权限',
    `description` text COMMENT '描述',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`     tinyint                                                       DEFAULT '0' COMMENT '是否删除，默认0-未删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 35
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='菜单表';