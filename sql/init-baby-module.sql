-- =============================================
-- BabyBuddy 婴儿跟踪平台 - MySQL 初始化脚本
-- =============================================
-- 执行前提：先执行 RuoYi 系统脚本
--   1. sql/ry_20250522.sql    (系统表+数据)
--   2. sql/quartz.sql          (Quartz 调度表)
-- 然后再执行本脚本（婴儿模块表+菜单权限）
-- =============================================

-- ============================
-- Part 1: 婴儿模块数据表
-- ============================

-- 婴儿信息表
CREATE TABLE IF NOT EXISTS bb_baby (
    baby_id      int primary key auto_increment,
    name         varchar(100) NOT NULL DEFAULT '',
    gender       varchar(10) DEFAULT '',
    birth_date   datetime,
    photo        varchar(500) DEFAULT '',
    create_by    varchar(64) DEFAULT '',
    create_time  datetime,
    update_by    varchar(64) DEFAULT '',
    update_time  datetime,
    remark       varchar(500) DEFAULT NULL
) engine=innodb;

-- 行为记录表
CREATE TABLE IF NOT EXISTS bb_behavior (
    behavior_id   int primary key auto_increment,
    baby_id       int DEFAULT NULL,
    behavior_type varchar(20) NOT NULL,
    feed_method   varchar(20) DEFAULT NULL,
    feed_amount   double DEFAULT NULL,
    diaper_type   varchar(20) DEFAULT NULL,
    play_type     varchar(20) DEFAULT NULL,
    start_time    datetime,
    end_time      datetime,
    duration      int DEFAULT NULL,
    create_by     varchar(64) DEFAULT '',
    create_time   datetime,
    update_by     varchar(64) DEFAULT '',
    update_time   datetime,
    remark        varchar(500) DEFAULT NULL
) engine=innodb;

-- 生长测量表
CREATE TABLE IF NOT EXISTS bb_measurement (
    measure_id   int primary key auto_increment,
    baby_id      int DEFAULT NULL,
    measure_type varchar(20) NOT NULL,
    value        double NOT NULL,
    measure_date datetime NOT NULL,
    create_by    varchar(64) DEFAULT '',
    create_time  datetime,
    update_by    varchar(64) DEFAULT '',
    update_time  datetime,
    remark       varchar(500) DEFAULT NULL
) engine=innodb;

-- ============================
-- Part 2: 婴儿模块菜单权限
-- ============================

-- 婴儿管理目录
INSERT INTO sys_menu VALUES('2000', '婴儿管理', '0', '5', 'baby', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', NULL, '');

-- 仪表盘
INSERT INTO sys_menu VALUES('2001', '首页仪表盘', '2000', '1', 'dashboard', 'baby/dashboard', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:dashboard:query', '#', 'admin', sysdate(), '', NULL, '');

-- 婴儿信息
INSERT INTO sys_menu VALUES('2002', '婴儿信息', '2000', '2', 'info', 'baby/info', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:info:query', '#', 'admin', sysdate(), '', NULL, '');

-- 行为管理目录
INSERT INTO sys_menu VALUES('2003', '行为管理', '2000', '3', 'behavior', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '#', 'admin', sysdate(), '', NULL, '');

-- 行为记录
INSERT INTO sys_menu VALUES('2004', '行为记录', '2003', '1', 'behavior-list', 'baby/behavior/index', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:behavior:list', '#', 'admin', sysdate(), '', NULL, '');

-- 生长数据目录
INSERT INTO sys_menu VALUES('2005', '生长数据', '2000', '4', 'measurement', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '#', 'admin', sysdate(), '', NULL, '');

-- 测量记录
INSERT INTO sys_menu VALUES('2006', '测量记录', '2005', '1', 'measurement-list', 'baby/measurement/index', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:measurement:list', '#', 'admin', sysdate(), '', NULL, '');

-- 计时器
INSERT INTO sys_menu VALUES('2007', '计时器', '2000', '5', 'timer', 'baby/timer/index', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:timer:use', '#', 'admin', sysdate(), '', NULL, '');

-- 报表目录
INSERT INTO sys_menu VALUES('2008', '报表中心', '2000', '6', 'report', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', '#', 'admin', sysdate(), '', NULL, '');

-- 生长曲线
INSERT INTO sys_menu VALUES('2009', '生长曲线', '2008', '1', 'growth', 'baby/report/growth', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:report:growth', '#', 'admin', sysdate(), '', NULL, '');

-- 行为间隔
INSERT INTO sys_menu VALUES('2010', '行为间隔', '2008', '2', 'feeding-interval', 'baby/report/feedingInterval', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:report:interval', '#', 'admin', sysdate(), '', NULL, '');

-- 每日喂养量
INSERT INTO sys_menu VALUES('2011', '每日喂养量', '2008', '3', 'daily-feeding', 'baby/report/dailyFeeding', NULL, NULL, 1, 0, 'C', '0', '0', 'baby:report:feeding', '#', 'admin', sysdate(), '', NULL, '');

-- 婴儿信息操作权限
INSERT INTO sys_menu VALUES('2012', '婴儿信息新增', '2002', '1', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:info:add',        '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2013', '婴儿信息修改', '2002', '2', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:info:edit',       '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2014', '婴儿信息删除', '2002', '3', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:info:remove',     '#', 'admin', sysdate(), '', NULL, '');

-- 行为操作权限
INSERT INTO sys_menu VALUES('2015', '行为新增',   '2004', '1', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:behavior:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2016', '行为修改',   '2004', '2', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:behavior:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2017', '行为删除',   '2004', '3', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:behavior:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 测量操作权限
INSERT INTO sys_menu VALUES('2018', '测量新增',   '2006', '1', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:measurement:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2019', '测量修改',   '2006', '2', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:measurement:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2020', '测量删除',   '2006', '3', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:measurement:remove', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- Part 3: 给管理员角色分配所有婴儿模块权限
-- ============================
INSERT INTO sys_role_menu SELECT '1', menu_id FROM sys_menu WHERE menu_id BETWEEN 2000 AND 2020
ON DUPLICATE KEY UPDATE menu_id = menu_id;