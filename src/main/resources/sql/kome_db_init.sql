
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(255) NOT NULL COMMENT '加密后的密码 (BCrypt)',
  `nickname` varchar(50) DEFAULT NULL COMMENT '展示昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `description` varchar(255) DEFAULT NULL COMMENT '个人简介 (侧边栏展示)',
  `is_owner` tinyint NOT NULL DEFAULT '0' COMMENT '是否为站点所有者: 0=否, 1=是',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除: 0=正常, 1=已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `slug` varchar(255) NOT NULL COMMENT '文章别名',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
  `content` longtext NOT NULL COMMENT 'Markdown原始内容',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图URL',
  `views` int unsigned NOT NULL DEFAULT '0' COMMENT '阅读量',
  `read_time` int unsigned NOT NULL DEFAULT '0' COMMENT '阅读耗时(分钟)',
  `is_pinned` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶: 0=否, 1=是',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态: 0=草稿, 1=已发布',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除: 0=正常, 1=已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_slug` (`slug`),
  KEY `idx_status_createtime` (`status`,`create_time` DESC),
  KEY `idx_create_time` (`create_time` DESC),
  FULLTEXT KEY `ft_idx_title_summary` (`title`,`summary`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章主表';

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- ----------------------------
-- Table structure for post_tag
-- ----------------------------
DROP TABLE IF EXISTS `post_tag`;
CREATE TABLE `post_tag` (
  `post_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关联创建时间',
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `idx_tag_id` (`tag_id`),
  CONSTRAINT `fk_pt_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pt_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章与标签多对多关联表';

-- ----------------------------
-- Table structure for memo
-- ----------------------------
DROP TABLE IF EXISTS `memo`;
CREATE TABLE `memo` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `content` text NOT NULL COMMENT '纯文本内容',
  `is_pinned` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶: 0=否, 1=是',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态: 0=草稿, 1=已发布',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除: 0=正常, 1=已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短语/说说表';

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '站点名称',
  `url` varchar(255) NOT NULL COMMENT '站点链接',
  `avatar` varchar(255) DEFAULT NULL COMMENT '站点Logo URL',
  `description` varchar(255) DEFAULT NULL COMMENT '一句话描述',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态: 0=隐藏, 1=公开',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友情链接表';

-- ----------------------------
-- Data Initialization
-- ----------------------------
-- 插入管理员数据，用户名为 "admin"，密码为 "Admin123.."
INSERT INTO `user`
(id, username, password, nickname, avatar, email, description, is_owner, is_deleted, create_time, update_time)
VALUES
(1, 'admin', '$2a$10$BSwlDea05yboOEsyZUOCpu/GUaIn7HyN4XZefcOOhUiVhN68f4T3e', '超级管理员', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 'admin@example.com', 'Hello World', 1, 0, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;