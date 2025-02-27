INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'app', '应用', 1, sysdate(), 'SysPermissionType', 1);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'menu', '菜单', 1, sysdate(), 'SysPermissionType', 2);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'auth', '权限', 1, sysdate(), 'SysPermissionType', 3);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'report', '报表', 1, sysdate(), 'SysPermissionType', 4);

INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'local', '本地路由', 1, sysdate(), 'SysPermissionRouteType', 1);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'dynamic', '动态路由', 1, sysdate(), 'SysPermissionRouteType', 2);

INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'file', '文件', 1, sysdate(), 'SysFileType', 1);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'image', '图片', 1, sysdate(), 'SysFileType', 2);

INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'local', '本地', 1, sysdate(), 'SysFileLocation', 1);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'aliOss', '阿里云', 1, sysdate(), 'SysFileLocation', 2);

INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'flowable', '审批', 1, sysdate(), 'SysMessageType', 1);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'form', '表单', 1, sysdate(), 'SysMessageType', 2);
