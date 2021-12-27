CREATE TABLE `t_article` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题',
  `description` varchar(512) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '摘要',
  `content` longtext COLLATE utf8mb4_bin NOT NULL COMMENT '内容',
  `points` int(11) NOT NULL DEFAULT '10' COMMENT '阅读数',
  `topic_id` bigint(16) NOT NULL DEFAULT '0' COMMENT '主题ID',
  `create_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` int(11) NOT NULL DEFAULT '0' COMMENT '更新人ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是,0:否)',
  `publish_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发布状态(0:未发布,1:已发布)',
  `source_url` varchar(256) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '来源URL',
  `source_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1原创，2转载',
  `title_pic` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题图',
  `source_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '来源名',
  `flag_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '标识(1:首页列表,2:置顶)',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  `article_type` int(4) NOT NULL DEFAULT '0' COMMENT '内容类型',
  `seo_keys` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'SEO关键字',
  `seo_desc` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'SEO描述',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_topic` (`topic_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `t_article_label` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` bigint(16) NOT NULL COMMENT '文章ID',
  `label_id` int(11) NOT NULL COMMENT '标签ID',
  `create_id` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` int(11) NOT NULL DEFAULT '0' COMMENT '更新人ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` int(11) NOT NULL COMMENT '文章ID',
  `content` varchar(255) NOT NULL COMMENT '内容',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父ID',
  `create_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` int(11) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `user_name` varchar(255) NOT NULL DEFAULT '' COMMENT '评论人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_type` varchar(255) NOT NULL DEFAULT '',
  `config_key` varchar(255) NOT NULL DEFAULT '',
  `config_name` varchar(255) NOT NULL DEFAULT '',
  `config_content` longtext NOT NULL,
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_id` int(11) NOT NULL DEFAULT '0',
  `deleted` tinyint(3) NOT NULL DEFAULT '0',
  `config_status` tinyint(4) NOT NULL DEFAULT '0',
  `config_order` int(11) NOT NULL DEFAULT '0',
  `config_md5` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_label
-- ----------------------------
DROP TABLE IF EXISTS `t_label`;
CREATE TABLE `t_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '标签名',
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_id` int(11) NOT NULL DEFAULT '0',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签';

CREATE TABLE `t_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `source` tinyint(4) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_id` int(11) NOT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_slider` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `pic` varchar(255) NOT NULL COMMENT '图片',
  `slider_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '使用类型见SliderTypeEnum',
  `target` varchar(255) NOT NULL DEFAULT '' COMMENT '跳转地址',
  `create_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` int(11) NOT NULL COMMENT '更新人ID ',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `slider_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态(0生效1失效)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_topic` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `topic_name` varchar(255) NOT NULL COMMENT '主题名',
  `topic_pic` varchar(255) DEFAULT NULL COMMENT '主题图',
  `topic_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '主题类型',
  `topic_sort` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '1导航2列表',
  `topic_position` tinyint(4) NOT NULL DEFAULT '1' COMMENT '主题位置',
  `create_id` int(11) NOT NULL DEFAULT '0',
  `update_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='主题';

CREATE TABLE `t_upload_file` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_id` int(11) NOT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `source` tinyint(4) NOT NULL COMMENT '来源',
  `md5` varchar(128) NOT NULL COMMENT 'MD5',
  `biz_id` varchar(255) DEFAULT NULL COMMENT '业务类型',
  `file_size` int(11) NOT NULL COMMENT '文件大小',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '用户名',
  `sex` tinyint(4) DEFAULT '0' COMMENT '性别',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `weixin` varchar(64) DEFAULT '' COMMENT '微信',
  `mobile` varchar(16) DEFAULT '' COMMENT '手机',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(128) NOT NULL DEFAULT '' COMMENT '盐',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态0－新注册，1正常，2锁住',
  `source` varchar(32) NOT NULL DEFAULT '' COMMENT '来源1:CRM,2:小程序，3：微信，4：PC',
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_id` int(11) NOT NULL DEFAULT '0',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `open_id` varchar(255) DEFAULT '' COMMENT '第三方ID',
  `role` tinyint(4) NOT NULL DEFAULT '1' COMMENT '角色',
  PRIMARY KEY (`id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';
