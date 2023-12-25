CALL add_system_simple_type('app', '应用', 'SysPermissionType', 1);
CALL add_system_simple_type('menu', '菜单', 'SysPermissionType', 2);
CALL add_system_simple_type('auth', '权限', 'SysPermissionType', 3);

CALL add_system_simple_type('file', '文件', 'SysFileType', 1);
CALL add_system_simple_type('image', '图片', 'SysFileType', 2);

CALL add_system_simple_type('local', '本地', 'SysFileLocation', 1);
CALL add_system_simple_type('aliOss', '阿里云', 'SysFileLocation', 2);