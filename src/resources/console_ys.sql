-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.13 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.2.0.4947
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 console_ys 的数据库结构
DROP DATABASE IF EXISTS `console_ys`;
CREATE DATABASE IF NOT EXISTS `console_ys` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `console_ys`;


-- 导出  表 console_ys.account 结构
DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `user_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `sex` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '0 女 1 男',
  `phone` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `status` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '0 无效 1 有效 2 删除  3  锁定',
  `depart` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `start_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `end_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `start_hour` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `description` text COLLATE utf8_bin,
  `remote_ip` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mac` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `ip_type` int(1) NOT NULL DEFAULT '1' COMMENT '选择是IP段还是IP/MAC:true(1)选IP段 false(0)选IP/MAC',
  `account_type` int(1) NOT NULL DEFAULT '2' COMMENT '区分是用户(2)还是管理员(1)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='账户表';

-- 正在导出表  console_ys.account 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
REPLACE INTO `account` (`id`, `user_name`, `password`, `sex`, `phone`, `created_time`, `modified_time`, `status`, `depart`, `title`, `name`, `email`, `start_ip`, `end_ip`, `start_hour`, `end_hour`, `description`, `remote_ip`, `mac`, `ip_type`, `account_type`) VALUES
	(1, 'admin', 'S8W2gMnH8VWiT9pXRMPQxA==', '1', '0571-88888888', '2010-07-04 13:52:36', '2012-09-12 16:50:46', '1', '信息中心', '主任', '初始化管理员', '**@hzih.net', '0.0.0.0', '192.168.200.254', 9, 18, '这是一个默认的超级用户信息', '192.168.2.176', '5C-63-BF-1D-72-07', 1, 1),
	(2, 'authadmin', 'S8W2gMnH8VWiT9pXRMPQxA==', '1', '0571-88888888', '2012-04-12 14:22:35', '2012-06-12 12:00:06', '1', '信息中心', '主任', '授权管理员', '**@hzih.net', '0.0.0.0', '192.168.200.254', 1, 22, '这是一个默认的授权用户信息', '', NULL, 1, 1),
	(3, 'configadmin', 'S8W2gMnH8VWiT9pXRMPQxA==', '1', '0571-88888888', '2012-06-12 18:04:01', '2012-06-12 18:23:16', '1', '信息中心', '主任', '配置管理员', '**@hzih.net', '0.0.0.0', '192.168.200.254', 9, 21, '这是一个默认的配置用户信息', '', NULL, 1, 1),
	(4, 'auditadmin', 'S8W2gMnH8VWiT9pXRMPQxA==', '1', '0571-88888888', '2012-07-03 10:19:57', '2015-08-26 14:02:56', '1', '信息中心', '主任', '审计管理员', 'hell@hzih.net', '0.0.0.0', '192.168.200.254', 7, 22, '这是一个默认的审计用户信息', NULL, NULL, 1, 1),
	(5, 'ABCDEF', 'mCG80k0ZGME=', '1', '66667777', '2015-12-02 11:23:15', NULL, '1', '信息部', '主任', '张三', 'hello@hzih.net', '0.0.0.1', '255.255.255.255', 9, 18, '这是一个用户信息', NULL, '', 1, 2);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;


-- 导出  表 console_ys.account_role 结构
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE IF NOT EXISTS `account_role` (
  `account_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_id`,`role_id`),
  KEY `FK410D0348FB5DAFD5` (`role_id`),
  KEY `FK410D0348870BFADF` (`account_id`),
  CONSTRAINT `FK410D0348870BFADF` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK410D0348FB5DAFD5` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='账户角色关联表';

-- 正在导出表  console_ys.account_role 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `account_role` DISABLE KEYS */;
REPLACE INTO `account_role` (`account_id`, `role_id`) VALUES
	(1, 1),
	(2, 2),
	(3, 3),
	(4, 4),
	(5, 6);
/*!40000 ALTER TABLE `account_role` ENABLE KEYS */;


-- 导出  表 console_ys.account_security 结构
DROP TABLE IF EXISTS `account_security`;
CREATE TABLE IF NOT EXISTS `account_security` (
  `id` bigint(20) NOT NULL,
  `isFilter` int(1) NOT NULL COMMENT '0:false,1:true',
  `isVirusScan` int(1) NOT NULL COMMENT '0:false,1:true',
  `infoLevel` varchar(100) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `appName` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `dir` varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户强制访问用表';

-- 正在导出表  console_ys.account_security 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `account_security` DISABLE KEYS */;
REPLACE INTO `account_security` (`id`, `isFilter`, `isVirusScan`, `infoLevel`, `appName`, `dir`) VALUES
	(1, 0, 0, '0', NULL, NULL),
	(2, 0, 0, '0', NULL, NULL),
	(3, 0, 0, '0', NULL, NULL),
	(4, 0, 0, '0', NULL, NULL),
	(5, 0, 0, '0', NULL, NULL);
/*!40000 ALTER TABLE `account_security` ENABLE KEYS */;


-- 导出  表 console_ys.audit_reset 结构
DROP TABLE IF EXISTS `audit_reset`;
CREATE TABLE IF NOT EXISTS `audit_reset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务名称',
  `business_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务类型',
  `file_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务对应文件全名',
  `file_size` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '文件大小',
  `import_time` datetime NOT NULL COMMENT '导入时间',
  `reset_status` int(4) NOT NULL DEFAULT '0' COMMENT '状态:0需重传1已经重传',
  `reset_count` int(10) NOT NULL DEFAULT '0' COMMENT '导入次数',
  `date_str` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务手动重传表';

-- 正在导出表  console_ys.audit_reset 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `audit_reset` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_reset` ENABLE KEYS */;


-- 导出  表 console_ys.business_log 结构
DROP TABLE IF EXISTS `business_log`;
CREATE TABLE IF NOT EXISTS `business_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL COMMENT '审计时间',
  `level` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '日志等级',
  `business_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务名称',
  `business_desc` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务描述',
  `business_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务类型（文件同步、UDP代理、TCP代理、数据库同步）\\n',
  `source_ip` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '源端地址',
  `source_port` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '源端端口',
  `source_jdbc` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '源端数据库',
  `dest_port` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '目标地址',
  `dest_ip` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '目标端口',
  `dest_jdbc` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '目标数据库',
  `audit_count` int(11) DEFAULT '0' COMMENT '审计量（数据库：条数 文件：文件尺寸大小 代理：字节数）		',
  `audit_count_ex` int(11) DEFAULT '0' COMMENT '(源端)审计量（数据库：条数 文件：文件尺寸大小 代理：字节数）		',
  `file_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件同步的文件全名',
  `plugin` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '源端external,目标internal',
  `json_id` int(11) DEFAULT '0' COMMENT '同一数据源在相同应用中的业务日志id',
  `json_id_ex` int(11) DEFAULT '0' COMMENT '同一数据源在相同应用中的业务日志id(源端)',
  `flag` int(4) NOT NULL DEFAULT '0' COMMENT '导出1未导出0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务日志审计表';

-- 正在导出表  console_ys.business_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `business_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_log` ENABLE KEYS */;


-- 导出  表 console_ys.business_log_handle_db 结构
DROP TABLE IF EXISTS `business_log_handle_db`;
CREATE TABLE IF NOT EXISTS `business_log_handle_db` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `appName` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '应用名',
  `fileName` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '临时文件名',
  `operate` varchar(8) COLLATE utf8_bin NOT NULL COMMENT 'internal(目标端) or external(源端)',
  `logTime` bigint(15) NOT NULL,
  `flag` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务日志处理,重传统计';

-- 正在导出表  console_ys.business_log_handle_db 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `business_log_handle_db` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_log_handle_db` ENABLE KEYS */;


-- 导出  表 console_ys.business_log_time 结构
DROP TABLE IF EXISTS `business_log_time`;
CREATE TABLE IF NOT EXISTS `business_log_time` (
  `appName` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '应用名',
  `average` double(10,2) NOT NULL COMMENT '一个临时文件的平均耗时',
  `count` int(10) NOT NULL COMMENT '平均耗时的统计次数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='统计目标端一个临时文件的平均耗时';

-- 正在导出表  console_ys.business_log_time 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `business_log_time` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_log_time` ENABLE KEYS */;


-- 导出  表 console_ys.business_security_alert 结构
DROP TABLE IF EXISTS `business_security_alert`;
CREATE TABLE IF NOT EXISTS `business_security_alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `business_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `alert_type_code` varchar(50) COLLATE utf8_bin NOT NULL,
  `ip` varchar(50) COLLATE utf8_bin NOT NULL,
  `user_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `isread` varchar(4) COLLATE utf8_bin NOT NULL DEFAULT 'N',
  `alert_info` varchar(500) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务异常报警信息';

-- 正在导出表  console_ys.business_security_alert 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `business_security_alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_security_alert` ENABLE KEYS */;


-- 导出  表 console_ys.business_security_alert_type 结构
DROP TABLE IF EXISTS `business_security_alert_type`;
CREATE TABLE IF NOT EXISTS `business_security_alert_type` (
  `code` varchar(10) COLLATE utf8_bin NOT NULL DEFAULT '',
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务异常代码';

-- 正在导出表  console_ys.business_security_alert_type 的数据：~10 rows (大约)
/*!40000 ALTER TABLE `business_security_alert_type` DISABLE KEYS */;
REPLACE INTO `business_security_alert_type` (`code`, `name`) VALUES
	('0000', ' 其它'),
	('0001', ' 数据流量异常'),
	('0002', ' 数据传输协议及端口异常'),
	('0003', ' 数据包结构异常'),
	('0004', ' 硬件设备运行情况异常'),
	('0005', ' 异常硬件设备类型'),
	('0006', ' 应用软件运行情况异常'),
	('0007', ' 异常应用软件名称'),
	('0008', ' 操作系统运行情况异常'),
	('0009', ' 数据库运行情况异常');
/*!40000 ALTER TABLE `business_security_alert_type` ENABLE KEYS */;


-- 导出  表 console_ys.contentfilter 结构
DROP TABLE IF EXISTS `contentfilter`;
CREATE TABLE IF NOT EXISTS `contentfilter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filter` varchar(500) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='内容过滤表';

-- 正在导出表  console_ys.contentfilter 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `contentfilter` DISABLE KEYS */;
REPLACE INTO `contentfilter` (`id`, `filter`) VALUES
	(1, '异常一'),
	(2, '替换二');
/*!40000 ALTER TABLE `contentfilter` ENABLE KEYS */;


-- 导出  表 console_ys.delete_status 结构
DROP TABLE IF EXISTS `delete_status`;
CREATE TABLE IF NOT EXISTS `delete_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appName` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '应用名',
  `plugin` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT 'external' COMMENT '数据源类型,external表示源端,internal表示目标端',
  `flagType` int(1) NOT NULL DEFAULT '0' COMMENT '标记等级',
  `deleteType` int(1) NOT NULL DEFAULT '0' COMMENT '0:不允许删除,1:允许删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='应用删除状态表';

-- 正在导出表  console_ys.delete_status 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `delete_status` DISABLE KEYS */;
REPLACE INTO `delete_status` (`id`, `appName`, `plugin`, `flagType`, `deleteType`) VALUES
	(1, 'file_1', 'external', 0, 0);
/*!40000 ALTER TABLE `delete_status` ENABLE KEYS */;


-- 导出  表 console_ys.department 结构
DROP TABLE IF EXISTS `department`;
CREATE TABLE IF NOT EXISTS `department` (
  `code` varchar(5) NOT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  console_ys.department 的数据：~32 rows (大约)
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
REPLACE INTO `department` (`code`, `name`) VALUES
	('00000', '信息通信部门'),
	('01000', '国内安全保卫管理部门'),
	('02000', '经济犯罪侦查管理部门'),
	('03000', '治安管理部门'),
	('04000', '边防管理部门'),
	('05000', '刑事侦查管理部门'),
	('06000', '出入境管理部门'),
	('07000', '消防管理部门'),
	('08000', '警卫管理部门'),
	('10000', '铁道公安管理部门'),
	('11000', '网络安全监察管理部门'),
	('12000', '行动技术管理部门 '),
	('13000', '监所管理部门'),
	('14000', '交通公安管理部门'),
	('15000', '民航公安管理部门'),
	('16000', '林业公安管理部门'),
	('17000', '交通管理部门'),
	('18000', '法制管理部门'),
	('19000', '外事管理部门'),
	('20000', '装备财务管理部门'),
	('21000', '禁毒管理部门'),
	('22000', '科技管理部门'),
	('23000', '基础和综合管理部门'),
	('24000', '海关公安管理部门'),
	('26000', '反邪教管理部门'),
	('27000', '反恐怖管理部门'),
	('31000', '办公管理部门(指挥中心管理部门)'),
	('32000', '纪委监察管理部门'),
	('34000', '督察管理部门'),
	('36000', '人事管理部门'),
	('39000', '离退休干部管理部门'),
	('92000', '其他');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;


-- 导出  表 console_ys.equipment 结构
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE IF NOT EXISTS `equipment` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `equipment_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `equipment_desc` varchar(255) COLLATE utf8_bin NOT NULL,
  `link_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `link_type` varchar(10) COLLATE utf8_bin NOT NULL,
  `equipment_type_code` varchar(6) COLLATE utf8_bin NOT NULL,
  `equipment_sys_config` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `equipment_manager_depart` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `monitor_used` varchar(2) COLLATE utf8_bin NOT NULL DEFAULT '1' COMMENT '是否开启监控 说明：0未开启，1开启',
  `key_device` varchar(2) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '是否核心设备 说明：1是，0否',
  `ip` varchar(20) COLLATE utf8_bin NOT NULL,
  `other_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `mac` varchar(20) COLLATE utf8_bin NOT NULL,
  `sub_net_mask` varchar(20) COLLATE utf8_bin NOT NULL,
  `port` varchar(20) COLLATE utf8_bin NOT NULL,
  `password` varchar(20) COLLATE utf8_bin NOT NULL,
  `oidname` varchar(50) COLLATE utf8_bin NOT NULL,
  `snmpver` varchar(50) COLLATE utf8_bin NOT NULL,
  `auth` varchar(50) COLLATE utf8_bin NOT NULL,
  `authpassword` varchar(50) COLLATE utf8_bin NOT NULL,
  `common` varchar(50) COLLATE utf8_bin NOT NULL,
  `commonpassword` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备表';

-- 正在导出表  console_ys.equipment 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
REPLACE INTO `equipment` (`Id`, `equipment_name`, `equipment_desc`, `link_name`, `link_type`, `equipment_type_code`, `equipment_sys_config`, `equipment_manager_depart`, `monitor_used`, `key_device`, `ip`, `other_ip`, `mac`, `sub_net_mask`, `port`, `password`, `oidname`, `snmpver`, `auth`, `authpassword`, `common`, `commonpassword`) VALUES
	(1, 'stp', '外网单向隔离光闸', 'extLinkName', 'ext', '2005', '', '01000', '1', '1', '1.1.1.2', '127.0.0.1', '38:60:77:4f:07:e2', '255.255.255.0', '161', 'public', 'linuxos', 'v2', '', '', '', '');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;


-- 导出  表 console_ys.equipment_alert 结构
DROP TABLE IF EXISTS `equipment_alert`;
CREATE TABLE IF NOT EXISTS `equipment_alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `equipment_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cpu` int(11) DEFAULT '80' COMMENT 'CPU告警阀值',
  `memory` int(11) DEFAULT '80' COMMENT '内存告警阀值',
  `disk` int(11) DEFAULT '90' COMMENT '硬盘告警阀值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备告警阀值信息';

-- 正在导出表  console_ys.equipment_alert 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `equipment_alert` DISABLE KEYS */;
REPLACE INTO `equipment_alert` (`id`, `equipment_name`, `cpu`, `memory`, `disk`) VALUES
	(1, 'stp', 100, 80, 90);
/*!40000 ALTER TABLE `equipment_alert` ENABLE KEYS */;


-- 导出  表 console_ys.equipment_log 结构
DROP TABLE IF EXISTS `equipment_log`;
CREATE TABLE IF NOT EXISTS `equipment_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL,
  `level` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `equipment_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `log_info` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备日志表';

-- 正在导出表  console_ys.equipment_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `equipment_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_log` ENABLE KEYS */;


-- 导出  表 console_ys.equipment_security_alert 结构
DROP TABLE IF EXISTS `equipment_security_alert`;
CREATE TABLE IF NOT EXISTS `equipment_security_alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `equipment_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `ip` varchar(50) COLLATE utf8_bin NOT NULL,
  `isread` varchar(4) COLLATE utf8_bin NOT NULL DEFAULT 'N',
  `alert_info` varchar(500) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备故障报警信息';

-- 正在导出表  console_ys.equipment_security_alert 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `equipment_security_alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_security_alert` ENABLE KEYS */;


-- 导出  表 console_ys.equipment_type 结构
DROP TABLE IF EXISTS `equipment_type`;
CREATE TABLE IF NOT EXISTS `equipment_type` (
  `code` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  console_ys.equipment_type 的数据：~43 rows (大约)
/*!40000 ALTER TABLE `equipment_type` DISABLE KEYS */;
REPLACE INTO `equipment_type` (`code`, `name`) VALUES
	('0000', '其它'),
	('1000', '安全及网络设备'),
	('1001', '防火墙'),
	('1002', '可信安全网关'),
	('1003', '入侵检测系统'),
	('1004', '网络防毒设备'),
	('1005', '安全隔离设备'),
	('1006', 'VPN网关'),
	('1007', '入侵防御系统'),
	('1008', ' AAA 服务器'),
	('1009', '漏洞扫描系统'),
	('1010', '边界安全监测设备'),
	('1011', '应用代理服务器'),
	('1012', '路由器'),
	('1013', '交换机'),
	('1014', '认证服务器'),
	('1015', '串口线'),
	('1030', '其他安全及网络设备'),
	('2000', '应用服务器'),
	('2001', 'WEB服务器'),
	('2002', 'FTP服务器'),
	('2003', '邮件服务器'),
	('2004', '数据库服务器'),
	('2005', '单向光闸外端机'),
	('2006', '单向光闸内端机'),
	('2020', '其他类型服务器'),
	('3000', '终端'),
	('3001', '台式计算机'),
	('3002', '笔记本电脑'),
	('3003', 'IP音视频终端'),
	('3004', '手持终端设备'),
	('3005', '其他终端设备'),
	('4000', 'IPSec VPN网关'),
	('4001', 'SSL VPN网关'),
	('4002', '短信接入网关'),
	('4003', 'B/S应用代理服务器'),
	('4004', 'B/S应用管理服务器'),
	('4005', '网络隔离设备'),
	('4006', ''),
	('4007', '设备证书管理中心'),
	('4008', '鉴别评估管理服务器'),
	('4009', '监控管理探针'),
	('4010', '监控管理与级联服务器');
/*!40000 ALTER TABLE `equipment_type` ENABLE KEYS */;


-- 导出  表 console_ys.ext_link 结构
DROP TABLE IF EXISTS `ext_link`;
CREATE TABLE IF NOT EXISTS `ext_link` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_property` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_Corp` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_security` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_amount` bigint(20) DEFAULT NULL,
  `link_bandwidth` bigint(20) DEFAULT NULL,
  `other_security` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='外部链路表';

-- 正在导出表  console_ys.ext_link 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `ext_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `ext_link` ENABLE KEYS */;


-- 导出  表 console_ys.interface 结构
DROP TABLE IF EXISTS `interface`;
CREATE TABLE IF NOT EXISTS `interface` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `interfaceNumber` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `tableName` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `tableName_EN` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `conditionsField` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `status` varchar(1) COLLATE utf8_bin DEFAULT '1',
  `apply` varchar(500) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='接口服务';

-- 正在导出表  console_ys.interface 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `interface` DISABLE KEYS */;
REPLACE INTO `interface` (`id`, `interfaceNumber`, `tableName`, `tableName_EN`, `conditionsField`, `url`, `status`, `apply`) VALUES
	(2, 'ABCDEFG', NULL, 'ZHXXB', 'GMSFHM,XM,XB,MZ,CSRQ,FWCS,DH,YDDH,HJDZ,XZZ,RESERVE93,ZP,URL', NULL, NULL, '');
/*!40000 ALTER TABLE `interface` ENABLE KEYS */;


-- 导出  表 console_ys.interface_account 结构
DROP TABLE IF EXISTS `interface_account`;
CREATE TABLE IF NOT EXISTS `interface_account` (
  `interfaceId` int(10) NOT NULL DEFAULT '0',
  `accountId` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`interfaceId`,`accountId`),
  KEY `FKC5D855274A079E7A` (`accountId`),
  KEY `FKC5D85527B41AD452` (`interfaceId`),
  KEY `FKC5D85527B9DA2FED` (`interfaceId`),
  CONSTRAINT `FKC5D855274A079E7A` FOREIGN KEY (`accountId`) REFERENCES `account` (`id`),
  CONSTRAINT `FKC5D85527B41AD452` FOREIGN KEY (`interfaceId`) REFERENCES `interface` (`id`),
  CONSTRAINT `FKC5D85527B9DA2FED` FOREIGN KEY (`interfaceId`) REFERENCES `interface` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  console_ys.interface_account 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `interface_account` DISABLE KEYS */;
REPLACE INTO `interface_account` (`interfaceId`, `accountId`) VALUES
	(2, 5);
/*!40000 ALTER TABLE `interface_account` ENABLE KEYS */;


-- 导出  表 console_ys.interface_htjk 结构
DROP TABLE IF EXISTS `interface_htjk`;
CREATE TABLE IF NOT EXISTS `interface_htjk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `info` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  console_ys.interface_htjk 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `interface_htjk` DISABLE KEYS */;
REPLACE INTO `interface_htjk` (`id`, `status`, `name`, `info`) VALUES
	(1, 1, '人口信息接口', '南通市局人口信息接口'),
	(2, 0, '照片信息接口', '南通市局照片信息接口');
/*!40000 ALTER TABLE `interface_htjk` ENABLE KEYS */;


-- 导出  表 console_ys.int_link 结构
DROP TABLE IF EXISTS `int_link`;
CREATE TABLE IF NOT EXISTS `int_link` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `jrdx` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `exchange_mode` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `link_bandwidth` int(11) DEFAULT NULL,
  `FW_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sec_gateway_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `gap_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VPN_used` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `other_security` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='内部链路表';

-- 正在导出表  console_ys.int_link 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `int_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `int_link` ENABLE KEYS */;


-- 导出  表 console_ys.manager_oper_log 结构
DROP TABLE IF EXISTS `manager_oper_log`;
CREATE TABLE IF NOT EXISTS `manager_oper_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL COMMENT '审计时间',
  `level` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '日志级别',
  `username` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '管理员账号名',
  `audit_module` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计模块',
  `audit_info` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计内容',
  `flag` int(1) NOT NULL DEFAULT '1' COMMENT '标记:1成功0失败',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2679 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理员操作审计表';

-- 正在导出表  console_ys.manager_oper_log 的数据：~970 rows (大约)
/*!40000 ALTER TABLE `manager_oper_log` DISABLE KEYS */;
REPLACE INTO `manager_oper_log` (`Id`, `log_time`, `level`, `username`, `audit_module`, `audit_info`, `flag`) VALUES
	(1686, '2015-10-20 11:15:12', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1687, '2015-10-20 11:15:13', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1688, '2015-10-20 11:15:23', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1689, '2015-10-20 11:15:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1690, '2015-10-20 11:15:27', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1691, '2015-10-20 11:15:28', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1692, '2015-10-20 11:15:33', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1693, '2015-10-20 11:15:38', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1694, '2015-10-20 11:15:39', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1695, '2015-10-20 11:17:03', 'INFO', 'test', '登录', '登录成功', 1),
	(1696, '2015-10-20 11:17:04', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1697, '2015-10-20 11:17:32', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1698, '2015-10-20 11:21:20', 'INFO', 'test', '登录', '登录成功', 1),
	(1699, '2015-10-20 11:21:21', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1700, '2015-10-20 11:21:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1701, '2015-10-20 11:27:32', 'INFO', 'test', '登录', '登录成功', 1),
	(1702, '2015-10-20 11:27:33', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1703, '2015-10-20 11:27:40', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1704, '2015-10-20 11:27:42', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1705, '2015-10-20 11:27:46', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1706, '2015-10-20 11:28:05', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1707, '2015-10-20 11:29:35', 'INFO', 'test', '登录', '登录成功', 1),
	(1708, '2015-10-20 11:29:36', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1709, '2015-10-20 11:30:04', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1710, '2015-10-20 11:30:12', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1711, '2015-10-20 11:30:29', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1712, '2015-10-20 11:33:03', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1713, '2015-10-20 11:34:04', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1714, '2015-10-20 11:34:06', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1715, '2015-10-20 11:34:18', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1716, '2015-10-20 11:37:35', 'INFO', 'test', '登录', '登录成功', 1),
	(1717, '2015-10-20 11:37:36', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1718, '2015-10-20 11:37:41', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1719, '2015-10-20 11:37:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1720, '2015-10-20 11:39:55', 'INFO', 'test', '登录', '登录成功', 1),
	(1721, '2015-10-20 11:39:56', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1722, '2015-10-20 11:40:23', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1723, '2015-10-20 11:40:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1724, '2015-10-20 11:40:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1725, '2015-10-20 11:44:05', 'INFO', 'test', '登录', '登录成功', 1),
	(1726, '2015-10-20 11:44:05', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1727, '2015-10-20 11:44:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1728, '2015-10-20 11:44:16', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1729, '2015-10-20 11:45:15', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1730, '2015-10-20 11:47:14', 'INFO', 'test', '登录', '登录成功', 1),
	(1731, '2015-10-20 11:47:15', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1732, '2015-10-20 11:47:18', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1733, '2015-10-20 11:47:25', 'ERROR', 'test', '接口服务', '删除接口服务信息失败null', 1),
	(1734, '2015-10-20 11:47:29', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1735, '2015-10-20 11:47:34', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1736, '2015-10-20 12:18:45', 'INFO', 'test', '登录', '退出成功', 1),
	(1737, '2015-10-20 12:19:02', 'INFO', 'test', '登录', '登录成功', 1),
	(1738, '2015-10-20 12:19:02', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1739, '2015-10-20 12:19:06', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1740, '2015-10-20 12:19:08', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1741, '2015-10-20 12:19:58', 'ERROR', 'test', '接口服务', '删除接口服务信息失败null', 1),
	(1742, '2015-10-20 12:20:57', 'INFO', 'test', '登录', '登录成功', 1),
	(1743, '2015-10-20 12:20:58', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1744, '2015-10-20 12:21:02', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1745, '2015-10-20 12:21:03', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1746, '2015-10-20 12:21:10', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1747, '2015-10-20 12:21:15', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1748, '2015-10-20 12:21:24', 'INFO', 'test', '接口服务', '删除接口服务接口服务111111成功', 1),
	(1749, '2015-10-20 12:21:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1750, '2015-10-20 12:22:49', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1751, '2015-10-20 12:22:51', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1752, '2015-10-20 12:22:58', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1753, '2015-10-20 12:37:28', 'INFO', 'test', '登录', '退出成功', 1),
	(1754, '2015-10-20 12:44:31', 'INFO', 'test', '登录', '登录成功', 1),
	(1755, '2015-10-20 12:44:32', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1756, '2015-10-20 12:44:46', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1757, '2015-10-20 12:44:48', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1758, '2015-10-20 12:44:49', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1759, '2015-10-20 12:45:13', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1760, '2015-10-20 12:45:19', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1761, '2015-10-20 12:45:25', 'INFO', 'test', '接口服务', '删除接口服务接口服务222222成功', 1),
	(1762, '2015-10-20 12:47:17', 'ERROR', 'test', '接口服务', '获取授权接口服务2的用户列表失败null', 1),
	(1763, '2015-10-20 12:47:21', 'ERROR', 'test', '接口服务', '获取授权接口服务2的用户列表失败null', 1),
	(1764, '2015-10-20 12:50:15', 'INFO', 'test', '登录', '登录成功', 1),
	(1765, '2015-10-20 12:50:16', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1766, '2015-10-20 12:50:20', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1767, '2015-10-20 12:50:22', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1768, '2015-10-20 12:50:24', 'ERROR', 'test', '接口服务', '删除接口服务信息失败null', 1),
	(1769, '2015-10-20 12:50:25', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1770, '2015-10-20 12:50:27', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1771, '2015-10-20 12:50:36', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1772, '2015-10-20 12:50:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1773, '2015-10-20 12:50:39', 'INFO', 'test', '接口服务', '删除接口服务接口服务111111成功', 1),
	(1774, '2015-10-20 12:50:40', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1775, '2015-10-20 12:50:42', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1776, '2015-10-20 12:56:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1777, '2015-10-20 13:00:18', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1778, '2015-10-20 13:00:19', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1779, '2015-10-20 13:00:28', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1780, '2015-10-20 13:00:31', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1781, '2015-10-20 13:00:33', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1782, '2015-10-20 13:00:34', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1783, '2015-10-20 13:02:29', 'INFO', 'test', '登录', '登录成功', 1),
	(1784, '2015-10-20 13:02:30', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1785, '2015-10-20 13:02:35', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1786, '2015-10-20 13:02:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1787, '2015-10-20 13:02:38', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1788, '2015-10-20 13:02:48', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1789, '2015-10-20 13:02:50', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1790, '2015-10-20 13:02:53', 'INFO', 'test', '接口服务', '删除接口服务接口服务111111成功', 1),
	(1791, '2015-10-20 13:02:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1792, '2015-10-20 13:02:55', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1793, '2015-10-20 13:02:59', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1794, '2015-10-20 13:03:01', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1795, '2015-10-20 13:22:06', 'INFO', 'test', '登录', '退出成功', 1),
	(1796, '2015-10-20 13:29:21', 'INFO', 'test', '登录', '登录成功', 1),
	(1797, '2015-10-20 13:29:23', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1798, '2015-10-20 13:29:29', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1799, '2015-10-20 13:29:33', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1800, '2015-10-20 13:40:03', 'INFO', 'test', '登录', '登录成功', 1),
	(1801, '2015-10-20 13:40:04', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1802, '2015-10-20 13:40:08', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1803, '2015-10-20 13:40:10', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1804, '2015-10-20 13:42:21', 'INFO', 'test', '登录', '登录成功', 1),
	(1805, '2015-10-20 13:42:22', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1806, '2015-10-20 13:42:31', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1807, '2015-10-20 13:47:02', 'INFO', 'test', '登录', '登录成功', 1),
	(1808, '2015-10-20 13:47:03', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1809, '2015-10-20 13:47:07', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1810, '2015-10-20 13:47:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1811, '2015-10-20 13:57:30', 'INFO', 'test', '登录', '退出成功', 1),
	(1812, '2015-10-20 14:00:28', 'INFO', 'test', '登录', '登录成功', 1),
	(1813, '2015-10-20 14:00:29', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1814, '2015-10-20 14:00:32', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1815, '2015-10-20 14:00:34', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1816, '2015-10-20 14:00:35', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1817, '2015-10-20 14:00:59', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1818, '2015-10-20 14:01:01', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1819, '2015-10-20 14:02:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1820, '2015-10-20 14:12:59', 'INFO', 'test', '登录', '退出成功', 1),
	(1821, '2015-10-20 14:13:06', 'INFO', 'test', '登录', '登录成功', 1),
	(1822, '2015-10-20 14:13:07', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1823, '2015-10-20 14:13:10', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1824, '2015-10-20 14:13:11', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1825, '2015-10-20 14:14:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1826, '2015-10-20 14:14:10', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1827, '2015-10-20 14:15:25', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1828, '2015-10-20 14:15:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1829, '2015-10-20 14:15:55', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1830, '2015-10-20 14:15:56', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1831, '2015-10-20 14:16:46', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1832, '2015-10-20 14:17:56', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1833, '2015-10-20 14:17:57', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1834, '2015-10-20 14:21:29', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1835, '2015-10-20 14:21:30', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1836, '2015-10-20 15:40:23', 'INFO', 'test', '登录', '退出成功', 1),
	(1837, '2015-10-20 15:40:34', 'INFO', 'test', '登录', '登录成功', 1),
	(1838, '2015-10-20 15:40:34', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1839, '2015-10-20 15:40:38', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1840, '2015-10-20 15:40:40', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1841, '2015-10-20 18:04:02', 'INFO', 'test', '登录', '登录成功', 1),
	(1842, '2015-10-20 18:04:03', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1843, '2015-10-20 18:04:08', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1844, '2015-10-20 18:04:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1845, '2015-10-20 18:06:40', 'INFO', 'test', '登录', '登录成功', 1),
	(1846, '2015-10-20 18:06:41', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1847, '2015-10-20 18:06:44', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1848, '2015-10-20 18:06:46', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1849, '2015-10-21 09:24:09', 'INFO', 'test', '登录', '登录成功', 1),
	(1850, '2015-10-21 09:24:13', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1851, '2015-10-21 09:24:18', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1852, '2015-10-21 09:24:20', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(1853, '2015-10-21 09:24:20', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(1854, '2015-10-21 09:24:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1855, '2015-10-21 09:24:28', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1856, '2015-10-21 09:24:37', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(1857, '2015-10-21 09:24:38', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1858, '2015-10-21 09:24:40', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1859, '2015-10-21 09:24:49', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1860, '2015-10-21 09:24:57', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1861, '2015-10-21 09:24:59', 'INFO', 'test', '登录', '退出成功', 1),
	(1862, '2015-10-21 09:26:50', 'INFO', 'test', '登录', '登录成功', 1),
	(1863, '2015-10-21 09:26:51', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1864, '2015-10-21 09:26:56', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1865, '2015-10-21 09:26:58', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1866, '2015-10-21 09:27:00', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(1867, '2015-10-21 09:27:11', 'INFO', 'test', '登录', '退出成功', 1),
	(1868, '2015-10-21 09:27:22', 'INFO', 'zhangsan', '接口服务', '获取接口服务信息成功', 1),
	(1869, '2015-10-21 09:27:25', 'INFO', 'zhangsan', '接口服务', '获取接口服务信息成功', 1),
	(1870, '2015-10-21 09:27:27', 'INFO', 'zhangsan', '接口服务', '申请授权接口服务1成功', 1),
	(1871, '2015-10-21 09:27:28', 'INFO', 'zhangsan', '接口服务', '获取接口服务信息成功', 1),
	(1872, '2015-10-21 09:27:52', 'INFO', 'test', '登录', '登录成功', 1),
	(1873, '2015-10-21 09:27:53', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1874, '2015-10-21 09:27:56', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1875, '2015-10-21 09:27:58', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(1876, '2015-10-21 09:28:02', 'INFO', 'test', '接口服务', '授权null接口服务1成功', 1),
	(1877, '2015-10-21 09:28:03', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1878, '2015-10-21 09:28:03', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(1879, '2015-10-21 09:28:07', 'INFO', 'test', '登录', '退出成功', 1),
	(1880, '2015-10-21 09:28:20', 'INFO', 'zhangsan', '接口服务', '获取接口服务信息成功', 1),
	(1881, '2015-10-21 09:28:22', 'INFO', 'zhangsan', '接口服务', '获取接口服务信息成功', 1),
	(1882, '2015-10-21 10:13:21', 'INFO', 'test', '登录', '登录成功', 1),
	(1883, '2015-10-21 10:13:22', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1884, '2015-10-21 10:13:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1885, '2015-10-21 10:14:31', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1886, '2015-10-21 10:14:32', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1887, '2015-10-21 10:15:23', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1888, '2015-10-21 10:25:23', 'INFO', 'test', '登录', '登录成功', 1),
	(1889, '2015-10-21 10:25:24', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1890, '2015-10-21 10:26:11', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1891, '2015-10-21 12:54:33', 'INFO', 'test', '登录', '登录成功', 1),
	(1892, '2015-10-21 12:54:37', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1893, '2015-10-21 12:54:43', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1894, '2015-10-21 12:54:48', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1895, '2015-10-21 12:57:18', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1896, '2015-10-21 12:58:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1897, '2015-10-21 13:10:57', 'INFO', 'test', '登录', '登录成功', 1),
	(1898, '2015-10-21 13:10:58', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1899, '2015-10-21 13:11:01', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1900, '2015-10-21 13:11:04', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1901, '2015-10-21 13:11:05', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1902, '2015-10-21 13:15:41', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1903, '2015-10-21 13:20:41', 'INFO', 'test', '登录', '登录成功', 1),
	(1904, '2015-10-21 13:20:42', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1905, '2015-10-21 13:20:46', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1906, '2015-10-21 13:20:48', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1907, '2015-10-21 13:20:50', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1908, '2015-10-21 13:20:51', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1909, '2015-10-21 13:24:50', 'INFO', 'test', '登录', '登录成功', 1),
	(1910, '2015-10-21 13:24:51', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1911, '2015-10-21 13:24:56', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1912, '2015-10-21 13:24:58', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1913, '2015-10-21 13:24:59', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1914, '2015-10-21 13:25:00', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1915, '2015-10-21 13:25:02', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1916, '2015-10-21 13:30:09', 'INFO', 'test', '登录', '登录成功', 1),
	(1917, '2015-10-21 13:30:10', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1918, '2015-10-21 13:30:14', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1919, '2015-10-21 13:30:16', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1920, '2015-10-21 13:30:18', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1921, '2015-10-21 13:31:31', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1922, '2015-10-21 13:32:16', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1923, '2015-10-21 13:32:19', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1924, '2015-10-21 13:33:06', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1925, '2015-10-21 13:35:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1926, '2015-10-21 13:46:32', 'INFO', 'test', '登录', '登录成功', 1),
	(1927, '2015-10-21 13:46:33', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1928, '2015-10-21 13:46:38', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1929, '2015-10-21 13:46:40', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1930, '2015-10-21 13:46:41', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1931, '2015-10-21 13:46:49', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1932, '2015-10-21 13:46:52', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(1933, '2015-10-21 13:46:52', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(1934, '2015-10-21 13:47:12', 'INFO', 'test', '角色管理', '更新角色信息成功', 1),
	(1935, '2015-10-21 13:47:14', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1936, '2015-10-21 13:47:23', 'INFO', 'test', '登录', '登录成功', 1),
	(1937, '2015-10-21 13:47:24', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1938, '2015-10-21 13:52:15', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1939, '2015-10-21 13:59:05', 'INFO', 'test', '登录', '登录成功', 1),
	(1940, '2015-10-21 13:59:06', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1941, '2015-10-21 13:59:14', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1942, '2015-10-21 13:59:17', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(1943, '2015-10-21 13:59:17', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(1944, '2015-10-21 13:59:30', 'INFO', 'test', '角色管理', '更新角色信息成功', 1),
	(1945, '2015-10-21 13:59:32', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1946, '2015-10-21 13:59:40', 'INFO', 'test', '登录', '登录成功', 1),
	(1947, '2015-10-21 13:59:41', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1948, '2015-10-21 14:00:28', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1949, '2015-10-21 14:00:38', 'INFO', 'test', '登录', '登录成功', 1),
	(1950, '2015-10-21 14:00:40', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1951, '2015-10-21 14:01:34', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1952, '2015-10-21 14:01:47', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1953, '2015-10-21 14:01:50', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(1954, '2015-10-21 14:01:50', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(1955, '2015-10-21 14:03:12', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1956, '2015-10-21 14:04:10', 'INFO', 'test', '登录', '登录成功', 1),
	(1957, '2015-10-21 14:04:11', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1958, '2015-10-21 14:06:22', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1959, '2015-10-21 14:06:24', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(1960, '2015-10-21 14:06:24', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(1961, '2015-10-21 14:06:38', 'INFO', 'test', '角色管理', '更新角色信息成功', 1),
	(1962, '2015-10-21 14:06:40', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(1963, '2015-10-21 14:06:51', 'INFO', 'test', '登录', '登录成功', 1),
	(1964, '2015-10-21 14:06:52', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1965, '2015-10-21 14:06:56', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1966, '2015-10-21 14:11:19', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1967, '2015-10-21 14:11:24', 'ERROR', 'test', '接口服务', '删除接口服务信息失败Could not execute JDBC batch update; nested exception is org.hibernate.exception.ConstraintViolationException: Could not execute JDBC batch update', 1),
	(1968, '2015-10-21 14:11:24', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1969, '2015-10-21 14:11:27', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1970, '2015-10-21 14:11:29', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1971, '2015-10-21 14:11:33', 'ERROR', 'test', '接口服务', '删除接口服务信息失败Could not execute JDBC batch update; nested exception is org.hibernate.exception.ConstraintViolationException: Could not execute JDBC batch update', 1),
	(1972, '2015-10-21 14:11:33', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1973, '2015-10-21 14:11:38', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1974, '2015-10-21 14:11:46', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1975, '2015-10-21 14:11:48', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(1976, '2015-10-21 14:11:51', 'INFO', 'test', '接口服务', '解除zhangsan的授权接口服务1成功', 1),
	(1977, '2015-10-21 14:11:52', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(1978, '2015-10-21 14:11:55', 'ERROR', 'test', '接口服务', '删除接口服务信息失败null', 1),
	(1979, '2015-10-21 14:11:55', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1980, '2015-10-21 14:11:57', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1981, '2015-10-21 14:11:58', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1982, '2015-10-21 14:11:59', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1983, '2015-10-21 14:12:03', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1984, '2015-10-21 14:14:55', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1985, '2015-10-21 14:14:59', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1986, '2015-10-21 14:15:02', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1987, '2015-10-21 14:15:05', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1988, '2015-10-21 14:20:48', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1989, '2015-10-21 14:22:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1990, '2015-10-21 14:22:39', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1991, '2015-10-21 14:22:59', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1992, '2015-10-21 14:23:00', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1993, '2015-10-21 14:23:02', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1994, '2015-10-21 15:11:16', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1995, '2015-10-21 15:11:17', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(1996, '2015-10-21 15:12:04', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1997, '2015-10-21 15:13:17', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1998, '2015-10-21 15:13:25', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(1999, '2015-10-21 15:16:22', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2000, '2015-10-21 15:16:35', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2001, '2015-10-21 15:19:58', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2002, '2015-10-21 15:20:01', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2003, '2015-10-21 15:20:02', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2004, '2015-10-21 15:24:24', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2005, '2015-10-21 15:24:41', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2006, '2015-10-21 15:24:44', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2007, '2015-10-21 15:24:48', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2008, '2015-10-21 15:24:49', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2009, '2015-10-21 15:24:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2010, '2015-10-21 15:24:54', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2011, '2015-10-21 15:29:47', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2012, '2015-10-21 15:29:49', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2013, '2015-10-21 15:30:11', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2014, '2015-10-21 15:40:55', 'INFO', 'test', '登录', '登录成功', 1),
	(2015, '2015-10-21 15:40:56', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2016, '2015-10-21 15:41:03', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2017, '2015-10-21 15:41:05', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2018, '2015-10-21 15:41:05', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2019, '2015-10-21 15:41:15', 'INFO', 'test', '角色管理', '更新角色信息成功', 1),
	(2020, '2015-10-21 15:41:17', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2021, '2015-10-21 15:41:27', 'INFO', 'test', '登录', '登录成功', 1),
	(2022, '2015-10-21 15:41:27', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2023, '2015-10-21 15:51:48', 'INFO', 'test', '登录', '退出成功', 1),
	(2024, '2015-10-21 15:51:54', 'INFO', 'test', '登录', '登录成功', 1),
	(2025, '2015-10-21 15:51:55', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2026, '2015-10-21 16:00:53', 'INFO', 'test', '登录', '登录成功', 1),
	(2027, '2015-10-21 16:00:54', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2028, '2015-10-21 16:20:24', 'INFO', 'test', '登录', '登录成功', 1),
	(2029, '2015-10-21 16:20:25', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2030, '2015-10-21 17:23:54', 'INFO', 'test', '登录', '登录成功', 1),
	(2031, '2015-10-21 17:23:54', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2032, '2015-10-21 17:24:05', 'INFO', 'test', '接口服务', '保存字段配置成功', 1),
	(2033, '2015-10-21 17:31:00', 'INFO', 'test', '登录', '登录成功', 1),
	(2034, '2015-10-21 17:31:01', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2035, '2015-10-21 17:31:08', 'INFO', 'test', '接口服务', '保存字段配置成功', 1),
	(2036, '2015-10-21 17:39:19', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2037, '2015-10-21 17:39:23', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2038, '2015-10-21 17:47:05', 'INFO', 'test', '登录', '登录成功', 1),
	(2039, '2015-10-21 17:47:06', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2040, '2015-10-21 17:49:28', 'INFO', 'test', '登录', '登录成功', 1),
	(2041, '2015-10-21 17:49:29', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2042, '2015-10-21 17:56:52', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2043, '2015-10-22 09:40:59', 'INFO', 'test', '登录', '登录成功', 1),
	(2044, '2015-10-22 09:41:01', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2045, '2015-10-22 09:45:04', 'INFO', 'test', '登录', '登录成功', 1),
	(2046, '2015-10-22 09:45:05', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2047, '2015-10-22 09:49:03', 'INFO', 'test', '登录 ', ' 密码错误 ', 0),
	(2048, '2015-10-22 09:49:10', 'INFO', 'test', '登录', '登录成功', 1),
	(2049, '2015-10-22 09:49:12', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2050, '2015-10-22 10:21:26', 'INFO', 'test', '登录', '退出成功', 1),
	(2051, '2015-10-22 10:33:08', 'INFO', 'test', '登录', '登录成功', 1),
	(2052, '2015-10-22 10:33:09', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2053, '2015-10-22 10:36:52', 'INFO', 'test', '登录', '登录成功', 1),
	(2054, '2015-10-22 10:36:53', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2055, '2015-10-22 10:40:46', 'INFO', 'test', '登录', '登录成功', 1),
	(2056, '2015-10-22 10:40:47', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2057, '2015-10-22 10:43:18', 'INFO', 'test', '登录', '登录成功', 1),
	(2058, '2015-10-22 10:43:19', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2059, '2015-10-22 10:56:17', 'INFO', 'test', '登录', '登录成功', 1),
	(2060, '2015-10-22 10:56:18', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2061, '2015-10-22 11:02:16', 'INFO', 'test', '登录', '登录成功', 1),
	(2062, '2015-10-22 11:02:17', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2063, '2015-10-22 11:04:44', 'INFO', 'test', '登录', '登录成功', 1),
	(2064, '2015-10-22 11:04:45', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2065, '2015-10-22 11:11:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2066, '2015-10-22 11:13:00', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2067, '2015-10-22 11:13:03', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2068, '2015-10-22 11:13:05', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2069, '2015-10-22 11:13:08', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2070, '2015-10-22 11:13:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2071, '2015-10-22 15:33:45', 'INFO', 'test', '登录', '登录成功', 1),
	(2072, '2015-10-22 15:33:47', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2073, '2015-10-22 15:35:28', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2074, '2015-10-22 15:35:31', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2075, '2015-10-22 15:40:59', 'INFO', 'test', '登录', '登录成功', 1),
	(2076, '2015-10-22 15:41:04', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2077, '2015-10-22 15:43:49', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2078, '2015-10-22 15:55:40', 'INFO', 'test', '登录', '退出成功', 1),
	(2079, '2015-10-22 15:55:49', 'INFO', 'test', '登录', '登录成功', 1),
	(2080, '2015-10-22 15:55:49', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2081, '2015-10-22 16:01:32', 'INFO', 'test', '登录', '退出成功', 1),
	(2082, '2015-10-22 16:01:40', 'INFO', 'test', '登录', '登录成功', 1),
	(2083, '2015-10-22 16:01:40', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2084, '2015-10-22 16:06:14', 'INFO', 'test', '登录', '退出成功', 1),
	(2085, '2015-10-22 16:06:21', 'INFO', 'test', '登录', '登录成功', 1),
	(2086, '2015-10-22 16:06:21', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2087, '2015-10-22 16:11:28', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2088, '2015-10-22 16:11:31', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2089, '2015-10-22 16:11:35', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2090, '2015-10-22 16:11:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2091, '2015-10-22 16:12:49', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2092, '2015-10-22 16:13:16', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2093, '2015-10-22 16:13:18', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2094, '2015-10-22 16:13:20', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2095, '2015-10-22 17:36:52', 'INFO', 'test', '登录', '登录成功', 1),
	(2096, '2015-10-22 17:36:53', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2097, '2015-10-22 17:37:37', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2098, '2015-10-22 17:37:42', 'INFO', 'test', '登录', '退出成功', 1),
	(2099, '2015-10-22 17:37:54', 'INFO', 'test', '登录', '登录成功', 1),
	(2100, '2015-10-22 17:37:54', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2101, '2015-11-04 19:03:02', 'INFO', 'test', '登录', '登录成功', 1),
	(2102, '2015-11-04 19:03:04', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2103, '2015-11-04 19:03:14', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2104, '2015-11-04 19:03:30', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2105, '2015-11-04 19:03:42', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2106, '2015-11-04 19:09:08', 'INFO', 'test', '登录', '登录成功', 1),
	(2107, '2015-11-04 19:09:09', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2108, '2015-11-04 19:09:15', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2109, '2015-11-04 19:14:21', 'INFO', 'test', '登录', '登录成功', 1),
	(2110, '2015-11-04 19:14:21', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2111, '2015-11-04 19:14:27', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2112, '2015-11-04 19:14:32', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2113, '2015-11-04 19:14:32', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2114, '2015-11-04 19:14:48', 'INFO', 'test', '角色管理', '更新角色信息成功', 1),
	(2115, '2015-11-04 19:14:56', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2116, '2015-11-04 19:15:02', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2117, '2015-11-04 19:15:10', 'INFO', 'test', '登录', '退出成功', 1),
	(2118, '2015-11-04 19:15:20', 'INFO', 'test', '登录', '登录成功', 1),
	(2119, '2015-11-04 19:15:20', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2120, '2015-11-04 19:15:24', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2121, '2015-11-04 19:15:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2122, '2015-11-04 19:47:30', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2123, '2015-11-04 19:47:33', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2124, '2015-11-04 20:06:15', 'INFO', 'test', '登录', '登录成功', 1),
	(2125, '2015-11-04 20:06:16', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2126, '2015-11-04 20:10:51', 'INFO', 'test', '登录', '登录成功', 1),
	(2127, '2015-11-04 20:10:51', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2128, '2015-11-04 20:21:24', 'INFO', 'test', '登录', '登录成功', 1),
	(2129, '2015-11-04 20:21:25', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2130, '2015-11-04 20:23:46', 'INFO', 'test', '登录', '登录成功', 1),
	(2131, '2015-11-04 20:23:47', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2132, '2015-11-25 09:57:32', 'INFO', 'test', '登录 ', ' IP地址不允许访问 ', 0),
	(2133, '2015-11-25 09:57:51', 'INFO', 'test', '登录 ', ' IP地址不允许访问 ', 0),
	(2134, '2015-11-25 09:59:07', 'INFO', 'admin', '登录', '登录成功', 1),
	(2135, '2015-11-25 09:59:09', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2136, '2015-11-25 09:59:12', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2137, '2015-11-25 09:59:14', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2138, '2015-11-25 09:59:14', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2139, '2015-11-25 09:59:21', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2140, '2015-11-25 09:59:23', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2141, '2015-11-25 09:59:58', 'INFO', 'test', '登录', '登录成功', 1),
	(2142, '2015-11-25 09:59:58', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2143, '2015-11-25 10:01:17', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2144, '2015-11-25 10:01:20', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2145, '2015-12-01 21:16:38', 'INFO', 'test', '登录', '登录成功', 1),
	(2146, '2015-12-01 21:16:41', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2147, '2015-12-01 21:16:49', 'INFO', 'admin', '登录', '登录成功', 1),
	(2148, '2015-12-01 21:16:50', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2149, '2015-12-01 21:16:53', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2150, '2015-12-01 21:16:55', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2151, '2015-12-01 21:16:57', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2152, '2015-12-01 21:16:57', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2153, '2015-12-01 21:16:58', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2154, '2015-12-01 21:16:58', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2155, '2015-12-01 21:16:58', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2156, '2015-12-01 21:23:23', 'INFO', 'admin', '登录', '登录成功', 1),
	(2157, '2015-12-01 21:23:24', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2158, '2015-12-01 21:23:25', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2159, '2015-12-01 21:23:25', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2160, '2015-12-01 21:23:28', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2161, '2015-12-01 21:23:28', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2162, '2015-12-01 21:23:28', 'INFO', 'admin', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2163, '2015-12-01 21:23:34', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2164, '2015-12-01 21:23:35', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2165, '2015-12-01 21:23:37', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2166, '2015-12-01 21:23:38', 'INFO', 'admin', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2167, '2015-12-01 21:23:48', 'INFO', 'admin', '角色管理', '更新角色信息成功', 1),
	(2168, '2015-12-01 21:23:50', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2169, '2015-12-01 21:23:59', 'INFO', 'test', '登录', '登录成功', 1),
	(2170, '2015-12-01 21:24:00', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2171, '2015-12-01 21:24:03', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2172, '2015-12-01 21:24:06', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2173, '2015-12-01 21:24:08', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2174, '2015-12-01 21:24:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2175, '2015-12-01 21:26:09', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2176, '2015-12-01 21:26:10', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2177, '2015-12-01 21:26:11', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2178, '2015-12-01 21:26:12', 'INFO', 'test', '安全策略', '获取安全策略信息成功', 1),
	(2179, '2015-12-01 21:26:12', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2180, '2015-12-01 21:26:12', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2181, '2015-12-01 21:26:13', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2182, '2015-12-01 21:26:14', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2183, '2015-12-01 21:26:17', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2184, '2015-12-01 21:31:49', 'INFO', 'test', '登录', '登录成功', 1),
	(2185, '2015-12-01 21:31:50', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2186, '2015-12-01 21:38:15', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2187, '2015-12-01 21:38:43', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2188, '2015-12-01 21:39:02', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2189, '2015-12-01 21:39:12', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2190, '2015-12-01 21:39:15', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2191, '2015-12-01 21:39:18', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2192, '2015-12-01 21:39:21', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2193, '2015-12-01 21:39:42', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2194, '2015-12-01 21:39:47', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2195, '2015-12-01 21:39:48', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2196, '2015-12-01 21:40:04', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2197, '2015-12-01 21:40:08', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2198, '2015-12-01 21:40:08', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2199, '2015-12-01 21:41:22', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2200, '2015-12-01 21:41:23', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2201, '2015-12-01 21:41:29', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2202, '2015-12-01 21:41:31', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2203, '2015-12-01 21:41:35', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2204, '2015-12-01 21:41:35', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2205, '2015-12-01 21:41:37', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2206, '2015-12-01 21:41:40', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2207, '2015-12-01 21:41:41', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2208, '2015-12-01 21:41:42', 'INFO', 'test', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2209, '2015-12-01 21:41:47', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2210, '2015-12-01 21:41:47', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2211, '2015-12-01 21:42:07', 'INFO', 'test', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2212, '2015-12-01 21:42:10', 'INFO', 'test', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2213, '2015-12-01 21:42:18', 'INFO', 'test', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2214, '2015-12-01 21:42:28', 'INFO', 'test', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2215, '2015-12-01 21:42:39', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2216, '2015-12-01 21:42:53', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2217, '2015-12-01 21:43:21', 'INFO', 'test', '接口服务', '新增接口服务接口服务null成功', 1),
	(2218, '2015-12-01 21:43:23', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2219, '2015-12-01 21:43:25', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2220, '2015-12-01 21:43:58', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2221, '2015-12-01 21:50:00', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2222, '2015-12-01 21:50:51', 'INFO', 'test', '登录', '登录成功', 1),
	(2223, '2015-12-01 21:50:51', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2224, '2015-12-01 21:50:55', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2225, '2015-12-01 21:50:57', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2226, '2015-12-01 21:51:00', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2227, '2015-12-01 21:51:06', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2228, '2015-12-01 21:51:07', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2229, '2015-12-01 21:51:48', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2230, '2015-12-01 21:51:50', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2231, '2015-12-01 21:52:06', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2232, '2015-12-01 21:52:07', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2233, '2015-12-01 21:52:19', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2234, '2015-12-01 21:52:23', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2235, '2015-12-01 21:52:26', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2236, '2015-12-01 21:52:29', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2237, '2015-12-01 21:52:48', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2238, '2015-12-01 21:52:51', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2239, '2015-12-01 21:52:58', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2240, '2015-12-01 21:53:02', 'INFO', 'test', '接口服务', '授权null接口服务1成功', 1),
	(2241, '2015-12-01 21:53:03', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2242, '2015-12-01 21:53:08', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2243, '2015-12-01 21:53:18', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2244, '2015-12-01 21:53:21', 'INFO', 'test', '接口服务', '解除zhangsan的授权接口服务1成功', 1),
	(2245, '2015-12-01 21:53:23', 'INFO', 'test', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2246, '2015-12-01 21:53:35', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2247, '2015-12-01 21:53:38', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2248, '2015-12-02 08:15:30', 'INFO', 'test', '登录', '登录成功', 1),
	(2249, '2015-12-02 08:15:31', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2250, '2015-12-02 08:15:37', 'INFO', 'test', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2251, '2015-12-02 08:15:54', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2252, '2015-12-02 08:22:28', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2253, '2015-12-02 08:25:36', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2254, '2015-12-02 08:25:37', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2255, '2015-12-02 08:25:39', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2256, '2015-12-02 08:25:42', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2257, '2015-12-02 08:26:44', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2258, '2015-12-02 08:26:44', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2259, '2015-12-02 08:26:56', 'ERROR', 'test', '账号管理', '删除账户yanshi信息失败', 0),
	(2260, '2015-12-02 08:26:57', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2261, '2015-12-02 08:27:00', 'INFO', 'test', '账号管理', '删除账户test信息成功', 1),
	(2262, '2015-12-02 08:27:01', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2263, '2015-12-02 08:27:03', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2264, '2015-12-02 08:27:03', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2265, '2015-12-02 08:27:03', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2266, '2015-12-02 08:27:03', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2267, '2015-12-02 08:27:08', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2268, '2015-12-02 08:27:13', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2269, '2015-12-02 08:27:15', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2270, '2015-12-02 08:27:17', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2271, '2015-12-02 08:27:21', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2272, '2015-12-02 08:27:32', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2273, '2015-12-02 08:27:32', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2274, '2015-12-02 08:27:37', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2275, '2015-12-02 08:27:43', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2276, '2015-12-02 08:27:45', 'INFO', 'test', '角色管理', '删除角色信息成功', 1),
	(2277, '2015-12-02 08:27:46', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2278, '2015-12-02 08:27:50', 'INFO', 'test', '角色管理', '删除角色信息成功', 1),
	(2279, '2015-12-02 08:27:51', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2280, '2015-12-02 08:27:52', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2281, '2015-12-02 08:27:58', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2282, '2015-12-02 08:28:13', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2283, '2015-12-02 08:28:14', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2284, '2015-12-02 08:28:15', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2285, '2015-12-02 08:28:15', 'INFO', 'test', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2286, '2015-12-02 08:28:16', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2287, '2015-12-02 08:28:16', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2288, '2015-12-02 08:28:18', 'INFO', 'test', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2289, '2015-12-02 08:28:21', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2290, '2015-12-02 08:28:23', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2291, '2015-12-02 08:28:39', 'INFO', 'test', '角色管理', '获取所有角色类型名成功', 1),
	(2292, '2015-12-02 08:28:41', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2293, '2015-12-02 08:28:41', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2294, '2015-12-02 08:28:43', 'INFO', 'test', '安全策略', '获取安全策略信息成功', 1),
	(2295, '2015-12-02 08:28:44', 'INFO', 'test', '角色管理', '获取角色信息成功', 1),
	(2296, '2015-12-02 08:28:46', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2297, '2015-12-02 08:28:46', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2298, '2015-12-02 08:28:52', 'INFO', 'test', '账号管理', '用户检查用户名是否存在成功', 1),
	(2299, '2015-12-02 08:30:51', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2300, '2015-12-02 08:30:52', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2301, '2015-12-02 08:30:53', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2302, '2015-12-02 08:30:53', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2303, '2015-12-02 08:30:57', 'INFO', 'test', '账号管理', '用户校验密码是否符合规则成功', 1),
	(2304, '2015-12-02 08:33:31', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2305, '2015-12-02 08:33:31', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2306, '2015-12-02 08:33:32', 'INFO', 'test', '管理', '获取所有角色名成功', 1),
	(2307, '2015-12-02 08:33:32', 'INFO', 'test', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2308, '2015-12-02 08:33:44', 'INFO', 'test', '账号管理', '用户检查用户名是否存在成功', 1),
	(2309, '2015-12-02 08:34:06', 'WARN', 'test', '账号管理', '用户新增账户wangwu信息密码不符合规则', 0),
	(2310, '2015-12-02 08:37:34', 'INFO', 'test', '安全策略', '获取安全策略信息成功', 1),
	(2311, '2015-12-02 08:39:09', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2312, '2015-12-02 08:40:17', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2313, '2015-12-02 08:40:44', 'INFO', 'test', '接口服务', '获取接口服务信息成功', 1),
	(2314, '2015-12-02 08:40:47', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2315, '2015-12-02 08:40:53', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2316, '2015-12-02 08:40:57', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2317, '2015-12-02 08:40:58', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2318, '2015-12-02 08:40:59', 'INFO', 'test', '主页加载', '获取用户的权限信息成功', 1),
	(2319, '2015-12-02 08:43:32', 'INFO', 'admin', '登录', '登录成功', 1),
	(2320, '2015-12-02 08:43:32', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2321, '2015-12-02 08:43:37', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2322, '2015-12-02 08:43:39', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2323, '2015-12-02 08:43:40', 'INFO', 'admin', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2324, '2015-12-02 08:43:50', 'INFO', 'admin', '角色管理', '更新角色信息成功', 1),
	(2325, '2015-12-02 08:43:51', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2326, '2015-12-02 08:44:04', 'INFO', 'admin', '登录', '登录成功', 1),
	(2327, '2015-12-02 08:44:05', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2328, '2015-12-02 08:44:08', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2329, '2015-12-02 08:44:10', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2330, '2015-12-02 08:44:12', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2331, '2015-12-02 08:44:17', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2332, '2015-12-02 08:44:28', 'INFO', 'admin', '安全策略', '修改安全策略信息成功', 1),
	(2333, '2015-12-02 08:44:30', 'INFO', 'admin', '安全策略', '修改安全策略信息成功', 1),
	(2334, '2015-12-02 08:44:34', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2335, '2015-12-02 08:44:34', 'INFO', 'admin', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2336, '2015-12-02 08:44:43', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2337, '2015-12-02 08:44:51', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2338, '2015-12-02 08:44:52', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2339, '2015-12-02 08:44:52', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2340, '2015-12-02 08:44:55', 'INFO', 'admin', '账号管理', '用户检查用户名是否存在成功', 1),
	(2341, '2015-12-02 08:45:10', 'INFO', 'admin', '账号管理', '用户新增账户fdfd信息成功', 1),
	(2342, '2015-12-02 08:45:11', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2343, '2015-12-02 08:45:13', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2344, '2015-12-02 08:45:13', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2345, '2015-12-02 08:45:14', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2346, '2015-12-02 08:45:14', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2347, '2015-12-02 08:45:17', 'INFO', 'admin', '账号管理', '删除账户fdfd信息成功', 1),
	(2348, '2015-12-02 08:45:18', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2349, '2015-12-02 08:45:21', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2350, '2015-12-02 08:46:43', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2351, '2015-12-02 08:46:45', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2352, '2015-12-02 08:46:45', 'INFO', 'admin', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2353, '2015-12-02 08:46:49', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2354, '2015-12-02 08:46:53', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2355, '2015-12-02 08:46:53', 'INFO', 'admin', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2356, '2015-12-02 08:46:55', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2357, '2015-12-02 08:46:56', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2358, '2015-12-02 08:47:01', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2359, '2015-12-02 08:47:19', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2360, '2015-12-02 08:47:20', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2361, '2015-12-02 08:47:23', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2362, '2015-12-02 08:47:24', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2363, '2015-12-02 08:47:26', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2364, '2015-12-02 08:47:34', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2365, '2015-12-02 08:47:34', 'INFO', 'admin', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2366, '2015-12-02 08:47:40', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2367, '2015-12-02 08:47:40', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2368, '2015-12-02 08:47:50', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2369, '2015-12-02 08:47:51', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2370, '2015-12-02 08:47:53', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2371, '2015-12-02 08:47:53', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2372, '2015-12-02 08:48:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2373, '2015-12-02 08:48:14', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2374, '2015-12-02 08:48:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2375, '2015-12-02 08:50:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2376, '2015-12-02 08:50:50', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2377, '2015-12-02 08:50:51', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2378, '2015-12-02 08:50:55', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2379, '2015-12-02 08:50:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2380, '2015-12-02 08:51:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2381, '2015-12-02 08:51:17', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2382, '2015-12-02 08:51:18', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2383, '2015-12-02 08:51:19', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2384, '2015-12-02 08:52:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2385, '2015-12-02 08:52:17', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2386, '2015-12-02 08:52:18', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2387, '2015-12-02 08:52:18', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2388, '2015-12-02 08:52:19', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2389, '2015-12-02 08:52:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2390, '2015-12-02 08:52:51', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2391, '2015-12-02 08:53:15', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2392, '2015-12-02 08:53:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2393, '2015-12-02 08:53:17', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2394, '2015-12-02 08:54:15', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2395, '2015-12-02 08:54:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2396, '2015-12-02 08:55:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2397, '2015-12-02 08:56:00', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2398, '2015-12-02 08:56:02', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2399, '2015-12-02 08:56:59', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2400, '2015-12-02 08:57:04', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2401, '2015-12-02 08:57:10', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2402, '2015-12-02 08:57:25', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2403, '2015-12-02 08:58:53', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2404, '2015-12-02 08:59:01', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2405, '2015-12-02 08:59:02', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2406, '2015-12-02 08:59:02', 'INFO', 'admin', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2407, '2015-12-02 08:59:09', 'INFO', 'admin', '角色管理', '更新角色信息成功', 1),
	(2408, '2015-12-02 08:59:10', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2409, '2015-12-02 08:59:16', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2410, '2015-12-02 08:59:20', 'INFO', 'admin', '登录', '退出成功', 1),
	(2411, '2015-12-02 09:02:24', 'INFO', 'admin', '登录', '登录成功', 1),
	(2412, '2015-12-02 09:02:24', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2413, '2015-12-02 09:02:28', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2414, '2015-12-02 09:02:29', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2415, '2015-12-02 09:02:31', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2416, '2015-12-02 09:02:34', 'INFO', 'admin', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2417, '2015-12-02 09:02:39', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2418, '2015-12-02 09:02:40', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2419, '2015-12-02 09:02:44', 'INFO', 'admin', '账号管理', '删除账户zhangsan信息成功', 1),
	(2420, '2015-12-02 09:02:45', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2421, '2015-12-02 09:02:48', 'INFO', 'admin', '账号管理', '删除账户lisi信息成功', 1),
	(2422, '2015-12-02 09:02:49', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2423, '2015-12-02 09:06:02', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2424, '2015-12-02 09:06:10', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2425, '2015-12-02 09:06:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2426, '2015-12-02 09:06:12', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2427, '2015-12-02 09:06:12', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2428, '2015-12-02 09:17:21', 'INFO', 'admin', '登录', '退出成功', 1),
	(2429, '2015-12-02 09:18:42', 'INFO', 'admin', '登录', '登录成功', 1),
	(2430, '2015-12-02 09:18:42', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2431, '2015-12-02 09:31:54', 'INFO', 'admin', '登录', '退出成功', 1),
	(2432, '2015-12-02 09:32:07', 'INFO', 'admin', '登录', '登录成功', 1),
	(2433, '2015-12-02 09:32:07', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2434, '2015-12-02 09:37:36', 'INFO', 'admin', '登录', '登录成功', 1),
	(2435, '2015-12-02 09:37:38', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2436, '2015-12-02 09:41:16', 'INFO', 'admin', '登录', '登录成功', 1),
	(2437, '2015-12-02 09:41:17', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2438, '2015-12-02 09:46:05', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2439, '2015-12-02 09:46:14', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2440, '2015-12-02 09:46:15', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2441, '2015-12-02 09:50:26', 'INFO', 'admin', '登录', '登录成功', 1),
	(2442, '2015-12-02 09:50:28', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2443, '2015-12-02 09:50:45', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2444, '2015-12-02 09:50:46', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2445, '2015-12-02 09:52:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2446, '2015-12-02 09:54:00', 'INFO', 'admin', '登录', '登录成功', 1),
	(2447, '2015-12-02 09:54:01', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2448, '2015-12-02 09:54:07', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2449, '2015-12-02 09:54:09', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2450, '2015-12-02 09:54:44', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2451, '2015-12-02 09:54:47', 'INFO', 'admin', '接口服务', '获取授权接口服务1的用户列表成功', 1),
	(2452, '2015-12-02 09:54:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2453, '2015-12-02 09:55:03', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2454, '2015-12-02 09:55:34', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2455, '2015-12-02 09:55:37', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2456, '2015-12-02 10:09:59', 'INFO', 'admin', '登录', '登录成功', 1),
	(2457, '2015-12-02 10:10:01', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2458, '2015-12-02 10:10:10', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2459, '2015-12-02 10:10:10', 'INFO', 'admin', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2460, '2015-12-02 10:10:12', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2461, '2015-12-02 10:10:14', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2462, '2015-12-02 10:10:14', 'INFO', 'admin', '角色管理', '获取角色权限信息用于修改成功', 1),
	(2463, '2015-12-02 10:10:29', 'INFO', 'admin', '角色管理', '更新角色信息成功', 1),
	(2464, '2015-12-02 10:10:30', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2465, '2015-12-02 10:10:36', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2466, '2015-12-02 10:10:43', 'INFO', 'admin', '登录', '退出成功', 1),
	(2467, '2015-12-02 10:10:53', 'INFO', 'admin', '登录', '登录成功', 1),
	(2468, '2015-12-02 10:10:54', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2469, '2015-12-02 10:10:59', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2470, '2015-12-02 10:11:00', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2471, '2015-12-02 10:11:05', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2472, '2015-12-02 10:11:07', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2473, '2015-12-02 10:11:10', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2474, '2015-12-02 10:11:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2475, '2015-12-02 10:11:14', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2476, '2015-12-02 10:11:15', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2477, '2015-12-02 10:11:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2478, '2015-12-02 10:11:43', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2479, '2015-12-02 10:11:50', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2480, '2015-12-02 10:11:54', 'ERROR', 'admin', '接口服务', '删除接口服务信息失败null', 1),
	(2481, '2015-12-02 10:11:54', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2482, '2015-12-02 10:11:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2483, '2015-12-02 10:11:57', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2484, '2015-12-02 10:12:06', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2485, '2015-12-02 10:12:23', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2486, '2015-12-02 10:12:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2487, '2015-12-02 10:32:56', 'INFO', 'admin', '登录', '登录成功', 1),
	(2488, '2015-12-02 10:32:57', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2489, '2015-12-02 10:33:13', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2490, '2015-12-02 10:33:15', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2491, '2015-12-02 10:33:24', 'INFO', 'admin', '接口服务', '新增接口服务接口服务fdsfdsf成功', 1),
	(2492, '2015-12-02 10:33:25', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2493, '2015-12-02 10:33:26', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2494, '2015-12-02 10:36:33', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2495, '2015-12-02 10:36:47', 'INFO', 'admin', '接口服务', '新增接口服务接口服务afadfdaf成功', 1),
	(2496, '2015-12-02 10:36:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2497, '2015-12-02 10:37:01', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2498, '2015-12-02 10:37:02', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2499, '2015-12-02 10:37:11', 'INFO', 'admin', '接口服务', '新增接口服务接口服务fdsfdsfds成功', 1),
	(2500, '2015-12-02 10:37:12', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2501, '2015-12-02 10:37:13', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2502, '2015-12-02 10:44:55', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2503, '2015-12-02 10:44:55', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2504, '2015-12-02 10:44:57', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2505, '2015-12-02 10:44:59', 'INFO', 'admin', '安全策略', '获取安全策略信息成功', 1),
	(2506, '2015-12-02 10:45:00', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2507, '2015-12-02 10:45:00', 'INFO', 'admin', '账号管理', '管理员获取所有管理员账号信息成功', 1),
	(2508, '2015-12-02 10:45:02', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2509, '2015-12-02 10:45:05', 'INFO', 'admin', '角色管理', '获取所有角色类型名成功', 1),
	(2510, '2015-12-02 10:45:10', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2511, '2015-12-02 10:45:11', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2512, '2015-12-02 10:45:13', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2513, '2015-12-02 10:45:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2514, '2015-12-02 10:45:19', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2515, '2015-12-02 10:45:21', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2516, '2015-12-02 10:45:22', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2517, '2015-12-02 10:45:24', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2518, '2015-12-02 10:45:25', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2519, '2015-12-02 10:45:26', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2520, '2015-12-02 10:45:27', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2521, '2015-12-02 11:00:05', 'INFO', 'admin', '登录', '退出成功', 1),
	(2522, '2015-12-02 11:05:40', 'INFO', 'admin', '登录', '登录成功', 1),
	(2523, '2015-12-02 11:05:41', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2524, '2015-12-02 11:05:46', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2525, '2015-12-02 11:05:48', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2526, '2015-12-02 11:06:39', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2527, '2015-12-02 11:06:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2528, '2015-12-02 11:06:44', 'ERROR', 'admin', '接口服务', '删除接口服务信息失败null', 1),
	(2529, '2015-12-02 11:06:46', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2530, '2015-12-02 11:06:48', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2531, '2015-12-02 11:06:57', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2532, '2015-12-02 11:06:58', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2533, '2015-12-02 11:07:53', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2534, '2015-12-02 11:07:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2535, '2015-12-02 11:08:04', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2536, '2015-12-02 11:08:05', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2537, '2015-12-02 11:08:19', 'ERROR', 'admin', '接口服务', '新增接口服务接口服务fdsfdsf失败A JSONArray text must start with \'[\' at character 1 of GMSFHM', 1),
	(2538, '2015-12-02 11:08:20', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2539, '2015-12-02 11:08:22', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2540, '2015-12-02 11:14:34', 'INFO', 'admin', '登录', '登录成功', 1),
	(2541, '2015-12-02 11:14:35', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2542, '2015-12-02 11:14:40', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2543, '2015-12-02 11:14:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2544, '2015-12-02 11:15:13', 'INFO', 'admin', '接口服务', '新增接口服务接口服务fdsfds成功', 1),
	(2545, '2015-12-02 11:17:53', 'INFO', 'admin', '登录', '登录成功', 1),
	(2546, '2015-12-02 11:17:54', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2547, '2015-12-02 11:18:13', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2548, '2015-12-02 11:18:14', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2549, '2015-12-02 11:18:36', 'INFO', 'admin', '接口服务', '新增接口服务接口服务fdsfdsf成功', 1),
	(2550, '2015-12-02 11:20:57', 'INFO', 'admin', '登录', '登录成功', 1),
	(2551, '2015-12-02 11:20:57', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2552, '2015-12-02 11:21:02', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2553, '2015-12-02 11:21:04', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2554, '2015-12-02 11:21:09', 'INFO', 'admin', '接口服务', '新增接口服务接口服务fdsfdsfds成功', 1),
	(2555, '2015-12-02 11:21:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2556, '2015-12-02 11:21:44', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2557, '2015-12-02 11:21:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2558, '2015-12-02 11:21:50', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2559, '2015-12-02 11:21:51', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2560, '2015-12-02 11:22:03', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2561, '2015-12-02 11:22:13', 'INFO', 'admin', '接口服务', '删除接口服务接口服务fdsfdsfds成功', 1),
	(2562, '2015-12-02 11:22:13', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2563, '2015-12-02 11:22:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2564, '2015-12-02 11:22:23', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2565, '2015-12-02 11:22:43', 'INFO', 'admin', '接口服务', '新增接口服务接口服务ABCDEFG成功', 1),
	(2566, '2015-12-02 11:22:44', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2567, '2015-12-02 11:22:45', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2568, '2015-12-02 11:22:49', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2569, '2015-12-02 11:22:49', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2570, '2015-12-02 11:22:55', 'INFO', 'admin', '账号管理', '用户检查用户名是否存在成功', 1),
	(2571, '2015-12-02 11:23:02', 'INFO', 'admin', '账号管理', '用户校验密码是否符合规则成功', 1),
	(2572, '2015-12-02 11:23:16', 'INFO', 'admin', '账号管理', '用户新增账户ABCDEF信息成功', 1),
	(2573, '2015-12-02 11:23:17', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2574, '2015-12-02 11:23:19', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2575, '2015-12-02 11:23:19', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2576, '2015-12-02 11:23:31', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2577, '2015-12-02 11:23:37', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2578, '2015-12-02 11:23:38', 'INFO', 'admin', '接口服务', '获取授权接口服务2的用户列表成功', 1),
	(2579, '2015-12-02 11:23:42', 'INFO', 'admin', '接口服务', '授权null接口服务2成功', 1),
	(2580, '2015-12-02 11:23:44', 'INFO', 'admin', '接口服务', '获取授权接口服务2的用户列表成功', 1),
	(2581, '2015-12-02 11:23:46', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2582, '2015-12-02 11:23:50', 'INFO', 'admin', '接口服务', '获取授权接口服务2的用户列表成功', 1),
	(2583, '2015-12-02 11:23:55', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2584, '2015-12-02 11:23:55', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2585, '2015-12-02 11:23:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2586, '2015-12-02 11:23:58', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2587, '2015-12-02 11:26:53', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2588, '2015-12-02 11:26:56', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2589, '2015-12-02 11:54:22', 'INFO', 'admin', '登录', '退出成功', 1),
	(2590, '2015-12-02 11:54:37', 'INFO', 'admin', '登录', '登录成功', 1),
	(2591, '2015-12-02 11:54:38', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2592, '2015-12-02 11:54:43', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2593, '2015-12-02 11:54:45', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2594, '2015-12-02 11:54:47', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2595, '2015-12-02 11:54:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2596, '2015-12-02 11:56:43', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2597, '2015-12-02 11:57:07', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2598, '2015-12-02 11:57:08', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2599, '2015-12-02 11:57:39', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2600, '2015-12-02 11:57:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2601, '2015-12-02 11:57:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2602, '2015-12-02 11:57:52', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2603, '2015-12-02 11:58:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2604, '2015-12-02 11:58:27', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2605, '2015-12-02 11:58:33', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2606, '2015-12-02 11:58:35', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2607, '2015-12-02 11:58:37', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2608, '2015-12-02 11:58:39', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2609, '2015-12-02 11:58:39', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2610, '2015-12-02 11:58:42', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2611, '2015-12-02 11:58:44', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2612, '2015-12-02 11:58:44', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2613, '2015-12-02 11:58:45', 'INFO', 'admin', '角色管理', '获取角色信息成功', 1),
	(2614, '2015-12-02 11:58:50', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2615, '2015-12-02 11:59:27', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2616, '2015-12-02 11:59:35', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2617, '2015-12-02 11:59:42', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2618, '2015-12-02 11:59:44', 'INFO', 'admin', '接口服务', '获取授权接口服务2的用户列表成功', 1),
	(2619, '2015-12-02 11:59:48', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2620, '2015-12-02 12:38:23', 'INFO', 'admin', '登录', '退出成功', 1),
	(2621, '2015-12-02 12:38:32', 'INFO', 'admin', '登录', '登录成功', 1),
	(2622, '2015-12-02 12:38:33', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2623, '2015-12-02 12:38:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2624, '2015-12-02 12:39:03', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2625, '2015-12-02 12:39:04', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2626, '2015-12-02 12:39:09', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2627, '2015-12-02 12:39:30', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2628, '2015-12-02 12:41:09', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2629, '2015-12-02 12:41:11', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2630, '2015-12-02 12:41:23', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2631, '2015-12-02 12:41:26', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2632, '2015-12-02 12:41:27', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2633, '2015-12-02 12:42:24', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2634, '2015-12-02 12:42:26', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2635, '2015-12-02 13:27:26', 'INFO', 'admin', '登录', '登录成功', 1),
	(2636, '2015-12-02 13:27:28', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2637, '2015-12-02 13:27:34', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2638, '2015-12-02 13:27:36', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2639, '2015-12-02 13:27:37', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2640, '2015-12-02 13:27:45', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2641, '2015-12-02 13:27:49', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2642, '2015-12-02 13:47:10', 'INFO', 'admin', '登录', '登录成功', 1),
	(2643, '2015-12-02 13:47:11', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2644, '2015-12-02 13:47:58', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2645, '2015-12-02 13:48:00', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2646, '2015-12-02 13:48:03', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2647, '2015-12-02 13:48:05', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2648, '2015-12-02 13:48:08', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2649, '2015-12-02 13:48:10', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2650, '2015-12-02 13:48:40', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2651, '2015-12-02 13:58:23', 'INFO', 'admin', '登录', '登录成功', 1),
	(2652, '2015-12-02 13:58:24', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2653, '2015-12-02 14:03:00', 'INFO', 'admin', '登录', '登录成功', 1),
	(2654, '2015-12-02 14:03:01', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2655, '2015-12-02 14:07:55', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2656, '2015-12-02 14:08:23', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2657, '2015-12-02 14:08:27', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2658, '2015-12-02 14:08:43', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2659, '2015-12-02 14:08:46', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2660, '2015-12-02 14:08:52', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2661, '2015-12-02 14:09:05', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2662, '2015-12-02 14:09:07', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2663, '2015-12-02 14:09:18', 'INFO', 'admin', '用户日志审计', '读取用户日志审计信息成功 ', 1),
	(2664, '2015-12-02 14:09:19', 'INFO', 'admin', '管理员日志审计', '读取管理员日志审计信息成功 ', 1),
	(2665, '2015-12-02 14:09:26', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2666, '2015-12-02 14:09:27', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2667, '2015-12-02 15:33:21', 'INFO', 'admin', '登录', '登录成功', 1),
	(2668, '2015-12-02 15:33:28', 'INFO', 'admin', '主页加载', '获取用户的权限信息成功', 1),
	(2669, '2015-12-02 15:33:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2670, '2015-12-02 15:33:52', 'INFO', 'admin', '管理', '获取所有角色名成功', 1),
	(2671, '2015-12-02 15:33:53', 'INFO', 'admin', '账号管理', '管理员获取所有用户账号信息成功', 1),
	(2672, '2015-12-02 15:33:58', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2673, '2015-12-02 15:34:41', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2674, '2015-12-02 15:35:30', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2675, '2015-12-02 15:35:37', 'INFO', 'admin', '接口服务', '获取授权接口服务2的用户列表成功', 1),
	(2676, '2015-12-02 15:35:52', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2677, '2015-12-02 15:36:14', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1),
	(2678, '2015-12-02 15:36:16', 'INFO', 'admin', '接口服务', '获取接口服务信息成功', 1);
/*!40000 ALTER TABLE `manager_oper_log` ENABLE KEYS */;


-- 导出  表 console_ys.permission 结构
DROP TABLE IF EXISTS `permission`;
CREATE TABLE IF NOT EXISTS `permission` (
  `ID` bigint(20) NOT NULL DEFAULT '0',
  `CODE` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  `iconCls` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '图标标识',
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '链接url',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='权限表';

-- 正在导出表  console_ys.permission 的数据：~18 rows (大约)
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
REPLACE INTO `permission` (`ID`, `CODE`, `NAME`, `DESCRIPTION`, `PARENT_ID`, `SEQ`, `iconCls`, `url`) VALUES
	(100, 'TOP_QXGL', '成员管理', NULL, 0, 0, 'user', NULL),
	(101, 'SECOND_YHGL', '用户账号管理', NULL, 100, 1, NULL, 'pages/user/userIndex.jsp'),
	(102, 'SECOND_JSGL', '用户角色管理', NULL, 100, 2, NULL, 'pages/user/userRoleIndex.jsp'),
	(103, 'SECOND_AQCL', '安全策略', NULL, 100, 3, NULL, 'pages/user/safePolicy.jsp'),
	(104, 'SECOND_GLYGL', '管理员账号管理', NULL, 100, 4, NULL, 'pages/user/managerIndex.jsp'),
	(105, 'SECOND_GLYJSGL', '管理员角色管理', NULL, 100, 5, NULL, 'pages/user/managerRoleIndex.jsp'),
	(106, 'SECOND_JSLXGL', '角色类型管理', NULL, 100, 6, NULL, 'pages/user/roleType.jsp'),
	(107, 'SECOND_XGMM', '修改密码', NULL, 100, 7, NULL, 'pages/user/update_password.jsp'),
	(110, 'TOP_SJGL', '日志管理', NULL, 0, 0, 'sjgl', NULL),
	(111, 'SECOND_GLYRZ', '管理员日志', NULL, 110, 1, NULL, 'pages/audit/audit_manager.jsp'),
	(112, 'SECOND_YHRZ', '接口日志', NULL, 110, 2, NULL, 'pages/audit/audit_user.jsp'),
	(113, 'SECOND_HTRZ', '后台接口日志', NULL, 110, 3, NULL, 'pages/audit/audit_htjklog.jsp'),
	(120, 'TOP_FWGL', '资源管理', NULL, 0, 0, 'fwgl', NULL),
	(121, 'SECOND_JKFW', '管理接口', NULL, 120, 1, NULL, 'pages/service/interface.jsp'),
	(122, 'SECOND_BGL', '表管理', NULL, 120, 2, NULL, 'pages/service/tables.jsp'),
	(123, 'SECOND_JKFWYH', '用户接口', NULL, 120, 3, NULL, 'pages/service/interface_user.jsp'),
	(130, 'TOP_HTJK', '后台接口', '', 0, 0, 'xtgl', NULL),
	(131, 'SECOND_RYJK', '人员接口', NULL, 130, 1, NULL, 'pages/htjk/person_msg.jsp'),
	(132, 'SECOND_ZPJK', '照片接口', NULL, 130, 2, NULL, 'pages/htjk/photo_msg.jsp');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;


-- 导出  表 console_ys.role 结构
DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 NOT NULL,
  `roleType` int(5) NOT NULL,
  `description` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

-- 正在导出表  console_ys.role 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
REPLACE INTO `role` (`id`, `name`, `roleType`, `description`, `createdTime`, `modifiedTime`) VALUES
	(1, '初始化管理员', 1, '初始化管理员', '2010-07-04 15:07:08', '2015-12-02 10:10:22'),
	(2, '授权管理员', 2, '授权管理员', '2012-07-03 10:06:20', '2015-08-26 15:01:22'),
	(3, '配置管理员', 4, '配置管理员', '2012-03-14 12:33:05', '2012-09-29 15:28:03'),
	(4, '审计管理员', 3, '审计管理员', '2012-06-12 18:37:24', '2012-09-21 08:58:39'),
	(6, '配置用户', 5, '配置用户', '2015-08-25 13:47:38', '2015-12-02 08:59:09');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;


-- 导出  表 console_ys.role_permission 结构
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE IF NOT EXISTS `role_permission` (
  `permission_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `FKBD40D538FB5DAFD5` (`role_id`),
  KEY `FKBD40D53866C1F4F5` (`permission_id`),
  CONSTRAINT `FKBD40D53866C1F4F5` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FKBD40D538FB5DAFD5` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=sjis COLLATE=sjis_bin COMMENT='角色权限关联表';

-- 正在导出表  console_ys.role_permission 的数据：~21 rows (大约)
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
REPLACE INTO `role_permission` (`permission_id`, `role_id`) VALUES
	(100, 1),
	(101, 1),
	(102, 1),
	(103, 1),
	(104, 1),
	(105, 1),
	(106, 1),
	(107, 1),
	(110, 1),
	(111, 1),
	(112, 1),
	(113, 1),
	(120, 1),
	(121, 1),
	(122, 1),
	(123, 1),
	(130, 1),
	(131, 1),
	(132, 1),
	(120, 6),
	(123, 6);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;


-- 导出  表 console_ys.role_type 结构
DROP TABLE IF EXISTS `role_type`;
CREATE TABLE IF NOT EXISTS `role_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_bin NOT NULL,
  `permission` varchar(255) COLLATE utf8_bin NOT NULL,
  `autoUnLock` int(1) NOT NULL COMMENT '1 是 0 否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色类型表';

-- 正在导出表  console_ys.role_type 的数据：~7 rows (大约)
/*!40000 ALTER TABLE `role_type` DISABLE KEYS */;
REPLACE INTO `role_type` (`id`, `name`, `permission`, `autoUnLock`) VALUES
	(1, '初始化管理员', 'read,rename,delete', 1),
	(2, '授权管理员', 'read,rename,delete', 0),
	(3, '审计管理员', 'read', 0),
	(4, '配置管理员', 'read', 1),
	(5, '一级用户', 'read', 1),
	(6, '二级用户', 'read,rename', 1),
	(7, '三级用户', 'read,rename,delete', 0);
/*!40000 ALTER TABLE `role_type` ENABLE KEYS */;


-- 导出  表 console_ys.safe_event_security_alert 结构
DROP TABLE IF EXISTS `safe_event_security_alert`;
CREATE TABLE IF NOT EXISTS `safe_event_security_alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `obj_type` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'business or equipment',
  `alert_type_code` varchar(50) COLLATE utf8_bin NOT NULL,
  `ip` varchar(50) COLLATE utf8_bin NOT NULL,
  `isread` varchar(4) COLLATE utf8_bin NOT NULL DEFAULT 'N',
  `alert_info` varchar(500) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='安全事件报警信息';

-- 正在导出表  console_ys.safe_event_security_alert 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `safe_event_security_alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `safe_event_security_alert` ENABLE KEYS */;


-- 导出  表 console_ys.safe_event_security_alert_type 结构
DROP TABLE IF EXISTS `safe_event_security_alert_type`;
CREATE TABLE IF NOT EXISTS `safe_event_security_alert_type` (
  `code` varchar(5) COLLATE utf8_bin NOT NULL DEFAULT '',
  `name` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='安全事件报警类型';

-- 正在导出表  console_ys.safe_event_security_alert_type 的数据：~14 rows (大约)
/*!40000 ALTER TABLE `safe_event_security_alert_type` DISABLE KEYS */;
REPLACE INTO `safe_event_security_alert_type` (`code`, `name`) VALUES
	('0000', ' 其它'),
	('0001', ' 自然灾害类'),
	('0002', ' 人为破坏类'),
	('0003', ' 误操作类'),
	('0004', ' 网络攻击类'),
	('0005', ' 操作系统攻击类'),
	('0006', ' 数据库攻击类'),
	('0007', ' 病毒类'),
	('0008', ' 应用系统（软件）程序故障类'),
	('0009', ' 设备硬件故障类'),
	('0010', ' 黑客入侵类'),
	('0011', ' 越权访问类'),
	('0012', ' 应用系统攻击类'),
	('0013', '设备负载过高类');
/*!40000 ALTER TABLE `safe_event_security_alert_type` ENABLE KEYS */;


-- 导出  表 console_ys.safe_policy 结构
DROP TABLE IF EXISTS `safe_policy`;
CREATE TABLE IF NOT EXISTS `safe_policy` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `timeout` int(11) DEFAULT NULL,
  `passwordLength` int(11) DEFAULT NULL,
  `errorLimit` int(11) DEFAULT NULL,
  `remoteDisabled` tinyint(1) DEFAULT NULL,
  `macDisabled` tinyint(1) DEFAULT NULL,
  `passwordRules` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `lockTime` int(10) NOT NULL DEFAULT '24' COMMENT '锁定时间(小时)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='安全策略表';

-- 正在导出表  console_ys.safe_policy 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `safe_policy` DISABLE KEYS */;
REPLACE INTO `safe_policy` (`id`, `timeout`, `passwordLength`, `errorLimit`, `remoteDisabled`, `macDisabled`, `passwordRules`, `lockTime`) VALUES
	(1, 600, 0, 3, 0, 0, '^.{6,100}$', 2);
/*!40000 ALTER TABLE `safe_policy` ENABLE KEYS */;


-- 导出  表 console_ys.securitylevel 结构
DROP TABLE IF EXISTS `securitylevel`;
CREATE TABLE IF NOT EXISTS `securitylevel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `level_info` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '信息等级',
  `security_flag` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT 'DES' COMMENT '加密算法',
  `security_level` int(2) NOT NULL DEFAULT '0' COMMENT '加密强度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='加密等级表';

-- 正在导出表  console_ys.securitylevel 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `securitylevel` DISABLE KEYS */;
REPLACE INTO `securitylevel` (`id`, `level_info`, `security_flag`, `security_level`) VALUES
	(1, '0', 'AES', 0),
	(2, '1', 'AES', 1),
	(3, '2', 'AES', 2);
/*!40000 ALTER TABLE `securitylevel` ENABLE KEYS */;


-- 导出  表 console_ys.snmpoid 结构
DROP TABLE IF EXISTS `snmpoid`;
CREATE TABLE IF NOT EXISTS `snmpoid` (
  `name` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '',
  `oidtype` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '',
  `company` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `snmpver` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cpuuse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `disktotal` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `diskuse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `memtotal` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `memuse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `curconn` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  console_ys.snmpoid 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `snmpoid` DISABLE KEYS */;
REPLACE INTO `snmpoid` (`name`, `oidtype`, `company`, `snmpver`, `cpuuse`, `disktotal`, `diskuse`, `memtotal`, `memuse`, `curconn`) VALUES
	('lenovofirewall', 'firewall', 'lenovo', 'v2', '.1.3.6.1.4.1.9833.1.4.1.14', '.1.3.6.1.4.1.9833.1.4.3.1.6', '', '.1.3.6.1.4.1.9833.1.4.3.2.5', '.1.3.6.1.4.1.9833.1.4.2.6', '.0.100.101.20'),
	('linuxos', 'pcserver', 'pcserver', 'v2', '.1.3.6.1.4.1.2021.11.9.0', '.1.3.6.1.4.1.2021.9.1.6.1', '.1.3.6.1.4.1.2021.9.1.7.1', '.1.3.6.1.4.1.2021.4.5.0', '.1.3.6.1.4.1.2021.4.6.0', '1.3.6.1.2.1.6.4'),
	('netchinafirewall', 'firewall', 'netchina', 'v2', '.1.3.6.1.4.1.3000.2.2', '.1.3.6.1.4.1.3000.2.3', '.1.3.6.1.4.1.3000.2.4', '.1.3.6.1.4.1.3000.2.5', '.1.3.6.1.4.1.3000.2.9', '.1.3.6.1.4.1.3000.2.10'),
	('rdaps', 'appdirector', 'radware', 'v2', '.1.3.6.1.4.1.89.35.1.112', '.1.3.6.1.4.1.89.35.1.112', '.1.3.6.1.4.1.89.35.1.112', '.1.3.6.1.4.1.89.35.1.112', '.1.3.6.1.4.1.89.35.1.112', '.1.3.6.1.4.1.89.35.1.123.2'),
	('secworld', 'firewall', 'legendsec', 'v2', '.1.3.6.1.4.1.24968.1.3.9.0', '', '', '', '.1.3.6.1.4.1.24968.1.3.10.0', '.1.3.6.1.4.1.24968.1.3.8.0'),
	('windowsos', 'pcserver', 'pcserver', 'v2', '.1.3.6.1.2.1.25.3.3.1.2', '.1.3.6.1.2.1.25.2.3.1.5', '.1.3.6.1.2.1.25.2.3.1.6', '.1.3.6.1.2.1.25.2.2', '.1.3.6.1.2.1.25.2.3.1.6.6', '.1.3.6.2.1.6.4');
/*!40000 ALTER TABLE `snmpoid` ENABLE KEYS */;


-- 导出  表 console_ys.sys_log 结构
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE IF NOT EXISTS `sys_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL COMMENT '产生时间',
  `level` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '日志等级',
  `audit_module` varchar(40) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计模块',
  `audit_action` varchar(40) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计行为',
  `audit_info` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计内容',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统日志审计表';

-- 正在导出表  console_ys.sys_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
REPLACE INTO `sys_log` (`Id`, `log_time`, `level`, `audit_module`, `audit_action`, `audit_info`) VALUES
	(1, '2015-12-02 08:43:22', 'INFO', 'test', '登录', ' 用户名不存在的登陆');
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;


-- 导出  表 console_ys.tables 结构
DROP TABLE IF EXISTS `tables`;
CREATE TABLE IF NOT EXISTS `tables` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `name_EN` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `countAll` int(11) DEFAULT NULL,
  `countNew` int(11) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `nameEn` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  console_ys.tables 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tables` DISABLE KEYS */;
/*!40000 ALTER TABLE `tables` ENABLE KEYS */;


-- 导出  表 console_ys.type_check 结构
DROP TABLE IF EXISTS `type_check`;
CREATE TABLE IF NOT EXISTS `type_check` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appName` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '应用编号',
  `appType` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '应用类型',
  `up` int(4) NOT NULL COMMENT '0表示未修改应用,1表示已经回应审核信息',
  `description` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '审核批注内容',
  `redescription` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '回应批注内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='应用批注表';

-- 正在导出表  console_ys.type_check 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `type_check` DISABLE KEYS */;
/*!40000 ALTER TABLE `type_check` ENABLE KEYS */;


-- 导出  表 console_ys.user_oper_log 结构
DROP TABLE IF EXISTS `user_oper_log`;
CREATE TABLE IF NOT EXISTS `user_oper_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL COMMENT '审计时间',
  `level` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '日志级别',
  `username` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户名',
  `audit_module` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计模块',
  `audit_info` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审计内容',
  `flag` int(1) NOT NULL DEFAULT '1' COMMENT '标记:1成功0失败',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户操作审计表';

-- 正在导出表  console_ys.user_oper_log 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user_oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_oper_log` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
