-- 婴儿管理目录
INSERT INTO sys_menu VALUES('2000', '婴儿管理', '0', '1', 'baby', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', NULL, '');

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
INSERT INTO sys_menu VALUES('2021', '婴儿信息导出', '2002', '4', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:info:export',     '#', 'admin', sysdate(), '', NULL, '');

-- 更新婴儿信息菜单权限为list
UPDATE sys_menu SET perms='baby:info:list' WHERE menu_id='2002';

-- 行为操作权限
INSERT INTO sys_menu VALUES('2015', '行为新增',   '2004', '1', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:behavior:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2016', '行为修改',   '2004', '2', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:behavior:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2017', '行为删除',   '2004', '3', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:behavior:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 测量操作权限
INSERT INTO sys_menu VALUES('2018', '测量新增',   '2006', '1', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:measurement:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2019', '测量修改',   '2006', '2', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:measurement:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2020', '测量删除',   '2006', '3', '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'baby:measurement:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 婴儿模块菜单权限分配给管理员角色
INSERT INTO sys_role_menu SELECT '1', menu_id FROM sys_menu WHERE menu_id BETWEEN 2000 AND 2020 AND NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = '1' AND menu_id = sys_menu.menu_id);