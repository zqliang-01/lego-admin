
CALL add_system_permission('report', '数据统计', 'app', NULL, 'business-intelligence', 4, 'local');
CALL add_system_permission('report_definition', '报表管理', 'menu', 'report', 'icon-title', 41, 'local');
CALL add_system_permission('report_definition_read', '查看', 'auth', 'report_definition', NULL, 411, NULL);
CALL add_system_permission('report_definition_add', '新增', 'auth', 'report_definition', NULL, 412, NULL);
CALL add_system_permission('report_definition_delete', '删除', 'auth', 'report_definition', NULL, 413, NULL);
CALL add_system_permission('report_definition_update', '更新', 'auth', 'report_definition', NULL, 414, NULL);

CALL add_system_permission('report_design', '报表设计', 'menu', 'report', 'board', 42, 'local');
CALL add_system_permission('report_design_read', '查看', 'auth', 'report_definition', NULL, 421, NULL);
CALL add_system_permission('report_design_add', '新增', 'auth', 'report_definition', NULL, 422, NULL);
CALL add_system_permission('report_design_delete', '删除', 'auth', 'report_definition', NULL, 423, NULL);
CALL add_system_permission('report_design_update', '更新', 'auth', 'report_definition', NULL, 424, NULL);