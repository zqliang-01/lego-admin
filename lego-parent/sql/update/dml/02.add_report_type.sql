
CALL add_system_simple_type('report', '报表', 'SysPermissionType', 4);

UPDATE sys_config c SET c.VALUE = 'oa,manage,crm,report'
WHERE c.CODE = 'AppValidList';