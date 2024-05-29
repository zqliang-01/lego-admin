
CALL add_system_permission('manage', '管理后台', 'app', NULL, 'all', 1, 'local');
CALL add_system_permission('manage_system', '企业首页', 'menu', 'manage', 'enterprise', 10, 'local');
CALL add_system_permission('manage_system_read', '查看', 'auth', 'manage_system', NULL, 101, NULL);
CALL add_system_permission('manage_system_update', '修改', 'auth', 'manage_system', NULL, 102, NULL);

CALL add_system_permission('manage_configSet', '应用管理', 'menu', 'manage', 'all', 11, 'local');
CALL add_system_permission('manage_configSet_read', '查看', 'auth', 'manage_configSet', NULL, 111, NULL);
CALL add_system_permission('manage_configSet_update', '上下架', 'auth', 'manage_configSet', NULL, 112, NULL);

CALL add_system_permission('manage_users', '员工与部门管理', 'menu', 'manage', 's-seas', 12, 'local');
CALL add_system_permission('manage_users_read', '查看', 'auth', 'manage_users', NULL, 121, NULL);
CALL add_system_permission('manage_users_add', '新增', 'auth', 'manage_users', NULL, 122, NULL);
CALL add_system_permission('manage_users_update', '修改', 'auth', 'manage_users', NULL, 123, NULL);

CALL add_system_permission('manage_role', '角色权限管理', 'menu', 'manage', 'user', 13, 'local');
CALL add_system_permission('manage_role_read', '查看', 'auth', 'manage_role', NULL, 131, NULL);
CALL add_system_permission('manage_role_add', '新增', 'auth', 'manage_role', NULL, 132, NULL);
CALL add_system_permission('manage_role_update', '修改', 'auth', 'manage_role', NULL, 133, NULL);
CALL add_system_permission('manage_role_delete', '删除', 'auth', 'manage_role', NULL, 134, NULL);
CALL add_system_permission('manage_role_auth', '授权', 'auth', 'manage_role', NULL, 135, NULL);

CALL add_system_permission('manage_genTable', '代码生成', 'menu', 'manage', 'approval-11', 14, 'local');
CALL add_system_permission('manage_genTable_read', '查看', 'auth', 'manage_genTable', NULL, 141, NULL);
CALL add_system_permission('manage_genTable_sync', '同步', 'auth', 'manage_genTable', NULL, 142, NULL);
CALL add_system_permission('manage_genTable_update', '更新', 'auth', 'manage_genTable', NULL, 143, NULL);
CALL add_system_permission('manage_genTable_design', '设计', 'auth', 'manage_genTable', NULL, 144, NULL);

CALL add_system_permission('manage_customForm', '自定义表单', 'menu', 'manage', 'icon-list', 15, 'local');
CALL add_system_permission('manage_customForm_read', '查看', 'auth', 'manage_customForm', NULL, 151, NULL);
CALL add_system_permission('manage_customForm_update', '更新', 'auth', 'manage_customForm', NULL, 152, NULL);
CALL add_system_permission('manage_customForm_design', '设计', 'auth', 'manage_customForm', NULL, 153, NULL);

CALL add_system_permission('manage_permission', '菜单管理', 'menu', 'manage', 'icon-des', 16, 'local');
CALL add_system_permission('manage_permission_read', '查看', 'auth', 'manage_permission', NULL, 161, NULL);
CALL add_system_permission('manage_permission_add', '新增', 'auth', 'manage_permission', NULL, 162, NULL);
CALL add_system_permission('manage_permission_update', '修改', 'auth', 'manage_permission', NULL, 163, NULL);
CALL add_system_permission('manage_permission_delete', '删除', 'auth', 'manage_permission', NULL, 164, NULL);

CALL add_system_permission('manage_printTemplate', '打印模板管理', 'menu', 'manage', 'print', 17, 'local');
CALL add_system_permission('manage_printTemplate_read', '查看列表', 'auth', 'manage_printTemplate', NULL, 171, NULL);
CALL add_system_permission('manage_printTemplate_add', '新增', 'auth', 'manage_printTemplate', NULL, 172, NULL);
CALL add_system_permission('manage_printTemplate_delete', '删除', 'auth', 'manage_printTemplate', NULL, 173, NULL);
CALL add_system_permission('manage_printTemplate_update', '更新', 'auth', 'manage_printTemplate', NULL, 174, NULL);

CALL add_system_permission('manage_workflow', '审批流程管理', 'menu', 'manage', 'icon-workflow', 18, 'local');
CALL add_system_permission('manage_workflow_model', '模型管理', 'menu', 'manage_workflow', NULL, 181, 'local');
CALL add_system_permission('manage_workflow_model_add', '新增', 'auth', 'manage_workflow_model', NULL, 1810, NULL);
CALL add_system_permission('manage_workflow_model_update', '修改', 'auth', 'manage_workflow_model', NULL, 1811, NULL);
CALL add_system_permission('manage_workflow_model_delete', '删除', 'auth', 'manage_workflow_model', NULL, 1812, NULL);
CALL add_system_permission('manage_workflow_model_deploy', '部署', 'auth', 'manage_workflow_model', NULL, 1813, NULL);

CALL add_system_permission('manage_workflow_definition', '部署管理', 'menu', 'manage_workflow', NULL, 182, 'local');
CALL add_system_permission('manage_workflow_definition_update', '修改', 'auth', 'manage_workflow_definition', NULL, 1820, NULL);
CALL add_system_permission('manage_workflow_definition_delete', '删除', 'auth', 'manage_workflow_definition', NULL, 1821, NULL);

CALL add_system_permission('manage_sharding', '分表管理', 'menu', 'manage', 'icon-category-note', 19, 'local');
CALL add_system_permission('manage_sharding_config', '分片规则配置', 'menu', 'manage_sharding', NULL, 191,'local');
CALL add_system_permission('manage_sharding_config_read', '查看', 'auth', 'manage_sharding_config', NULL, 1910, NULL);
CALL add_system_permission('manage_sharding_config_add', '新增', 'auth', 'manage_sharding_config', NULL, 1911, NULL);
CALL add_system_permission('manage_sharding_config_update', '更新', 'auth', 'manage_sharding_config', NULL, 1912, NULL);

CALL add_system_permission('manage_sharding_table', '分片表配置', 'menu', 'manage_sharding', NULL, 192, 'local');
CALL add_system_permission('manage_sharding_table_read', '查看', 'auth', 'manage_sharding_table', NULL, 1920, NULL);
CALL add_system_permission('manage_sharding_table_add', '新增', 'auth', 'manage_sharding_table', NULL, 1921, NULL);
CALL add_system_permission('manage_sharding_table_update', '更新', 'auth', 'manage_sharding_table', NULL, 1922, NULL);

CALL add_system_permission('manage_sharding_algorithm', '分片算法配置', 'menu', 'manage_sharding', NULL, 193, 'local');
CALL add_system_permission('manage_sharding_algorithm_read', '查看', 'auth', 'manage_sharding_algorithm', NULL, 1930, NULL);
CALL add_system_permission('manage_sharding_algorithm_add', '新增', 'auth', 'manage_sharding_algorithm', NULL, 1931, NULL);
CALL add_system_permission('manage_sharding_algorithm_update', '更新', 'auth', 'manage_sharding_algorithm', NULL, 1932, NULL);

CALL add_system_permission('manage_sharding_dataSource', '分片数据源配置', 'menu', 'manage_sharding', NULL, 194, 'local');
CALL add_system_permission('manage_sharding_dataSource_read', '查看', 'auth', 'manage_sharding_dataSource', NULL, 1940, NULL);
CALL add_system_permission('manage_sharding_dataSource_add', '新增', 'auth', 'manage_sharding_dataSource', NULL, 1941, NULL);
CALL add_system_permission('manage_sharding_dataSource_delete', '删除', 'auth', 'manage_sharding_dataSource', NULL, 1942, NULL);
CALL add_system_permission('manage_sharding_dataSource_update', '更新', 'auth', 'manage_sharding_dataSource', NULL, 1943, NULL);

CALL add_system_permission('manage_sharding_template', '分片模板配置', 'menu', 'manage_sharding', NULL, 195, 'local');
CALL add_system_permission('manage_sharding_template_read', '查看', 'auth', 'manage_sharding_template', NULL, 1950, NULL);
CALL add_system_permission('manage_sharding_template_add', '新增', 'auth', 'manage_sharding_template', NULL, 1951, NULL);
CALL add_system_permission('manage_sharding_template_update', '更新', 'auth', 'manage_sharding_template', NULL, 1952, NULL);

CALL add_system_permission('manage_notice', '公告管理', 'menu', 'manage', 'announcement', 20, 'local');
CALL add_system_permission('manage_notice_read', '查看', 'auth', 'manage_notice', NULL, 201, NULL);
CALL add_system_permission('manage_notice_add', '新增', 'auth', 'manage_notice', NULL, 202, NULL);
CALL add_system_permission('manage_notice_publish', '发布', 'auth', 'manage_notice', NULL, 203, NULL);

CALL add_system_permission('manage_log', '日志管理', 'menu', 'manage', 'task', 21, 'local');
CALL add_system_permission('manage_log_read', '查看', 'auth', 'manage_log', NULL, 211, NULL);

CALL add_system_permission('oa', '任务审批', 'app', NULL, 'office', 3, 'local');
CALL add_system_permission('oa_start', '发起审批', 'menu', 'oa', 'top', 30, 'local');
CALL add_system_permission('oa_start_read', '查看列表', 'auth', 'oa_start', NULL, 301, NULL);

CALL add_system_permission('oa_owner', '我的流程', 'menu', 'oa', 'my-task', 31, 'local');
CALL add_system_permission('oa_owner_read', '查看列表', 'auth', 'oa_owner', NULL, 311, NULL);

CALL add_system_permission('oa_undo', '待办任务', 'menu', 'oa', 'contract', 32, 'local');
CALL add_system_permission('oa_undo_read', '查看列表', 'auth', 'oa_undo', NULL, 321, NULL);

CALL add_system_permission('oa_unclaimed', '待签任务', 'menu', 'oa', 'icon-related-tasks', 33, 'local');
CALL add_system_permission('oa_unclaimed_read', '查看列表', 'auth', 'oa_unclaimed', NULL, 331, NULL);

CALL add_system_permission('oa_finished', '已办任务', 'menu', 'oa', 'icon-task-state', 34, 'local');
CALL add_system_permission('oa_finished_read', '查看列表', 'auth', 'oa_finished', NULL, 341, NULL);