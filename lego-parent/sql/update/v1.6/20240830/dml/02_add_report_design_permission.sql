CALL add_system_permission('report_design', '首页设计', 'menu', 'report', 'board', 42, 'local');
CALL add_system_permission('report_design_read', '查看', 'auth', 'report_design', NULL, 421, NULL);
CALL add_system_permission('report_design_add', '新增', 'auth', 'report_design', NULL, 422, NULL);
CALL add_system_permission('report_design_delete', '删除', 'auth', 'report_design', NULL, 423, NULL);
CALL add_system_permission('report_design_update', '更新', 'auth', 'report_design', NULL, 424, NULL);