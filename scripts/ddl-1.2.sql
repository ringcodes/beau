CREATE TABLE `t_role_permit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role` int(255) NOT NULL DEFAULT '0' COMMENT '角色',
  `permit_code` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '权限码',
  `create_id` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_id` int(11) NOT NULL DEFAULT '0',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `permit_type` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色权限表';
