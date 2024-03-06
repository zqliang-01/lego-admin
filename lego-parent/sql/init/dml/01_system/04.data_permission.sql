
CALL add_system_permission('manage', '管理后台', 'app', NULL, 'all', 1, NULL);
CALL add_system_permission('manage_system', '企业首页', 'menu', 'manage', 'enterprise', 10, NULL);
CALL add_system_permission('manage_system_read', '查看', 'auth', 'manage_system', NULL, 101, NULL);
CALL add_system_permission('manage_system_update', '修改', 'auth', 'manage_system', NULL, 102, NULL);

CALL add_system_permission('manage_configSet', '应用管理', 'menu', 'manage', 'all', 11, NULL);
CALL add_system_permission('manage_configSet_read', '查看', 'auth', 'manage_configSet', NULL, 111, NULL);
CALL add_system_permission('manage_configSet_update', '上下架', 'auth', 'manage_configSet', NULL, 112, NULL);

CALL add_system_permission('manage_users', '员工与部门管理', 'menu', 'manage', 's-seas', 12, NULL);
CALL add_system_permission('manage_users_read', '查看', 'auth', 'manage_users', NULL, 121, NULL);
CALL add_system_permission('manage_users_add', '新增', 'auth', 'manage_users', NULL, 122, NULL);
CALL add_system_permission('manage_users_update', '修改', 'auth', 'manage_users', NULL, 123, NULL);

CALL add_system_permission('manage_role', '角色权限管理', 'menu', 'manage', 'user', 13, NULL);
CALL add_system_permission('manage_role_read', '查看', 'auth', 'manage_role', NULL, 131, NULL);
CALL add_system_permission('manage_role_add', '新增', 'auth', 'manage_role', NULL, 132, NULL);
CALL add_system_permission('manage_role_update', '修改', 'auth', 'manage_role', NULL, 133, NULL);
CALL add_system_permission('manage_role_delete', '删除', 'auth', 'manage_role', NULL, 134, NULL);
CALL add_system_permission('manage_role_auth', '授权', 'auth', 'manage_role', NULL, 135, NULL);

CALL add_system_permission('manage_genTable', '代码生成', 'menu', 'manage', 'approval-11', 14, NULL);
CALL add_system_permission('manage_genTable_read', '查看', 'auth', 'manage_genTable', NULL, 141, NULL);
CALL add_system_permission('manage_genTable_sync', '同步', 'auth', 'manage_genTable', NULL, 142, NULL);
CALL add_system_permission('manage_genTable_update', '更新', 'auth', 'manage_genTable', NULL, 143, NULL);
CALL add_system_permission('manage_genTable_design', '设计', 'auth', 'manage_genTable', NULL, 144, NULL);

CALL add_system_permission('manage_customForm', '自定义表单', 'menu', 'manage', 'icon-list', 15, NULL);
CALL add_system_permission('manage_customForm_read', '查看', 'auth', 'manage_customForm', NULL, 151, NULL);
CALL add_system_permission('manage_customForm_update', '更新', 'auth', 'manage_customForm', NULL, 152, NULL);
CALL add_system_permission('manage_customForm_design', '设计', 'auth', 'manage_customForm', NULL, 153, NULL);

CALL add_system_permission('manage_permission', '菜单管理', 'menu', 'manage', 'icon-des', 16, NULL);
CALL add_system_permission('manage_permission_read', '查看', 'auth', 'manage_permission', NULL, 161, NULL);
CALL add_system_permission('manage_permission_add', '新增', 'auth', 'manage_permission', NULL, 162, NULL);
CALL add_system_permission('manage_permission_update', '修改', 'auth', 'manage_permission', NULL, 163, NULL);
CALL add_system_permission('manage_permission_delete', '删除', 'auth', 'manage_permission', NULL, 164, NULL);

CALL add_system_permission('manage_workflow', '审批流程管理', 'menu', 'manage', 'icon-workflow', 17, NULL);
CALL add_system_permission('manage_workflow_model', '模型管理', 'menu', 'manage_workflow', NULL, 171, NULL);
CALL add_system_permission('manage_workflow_model_add', '新增', 'auth', 'manage_workflow_model', NULL, 1710, NULL);
CALL add_system_permission('manage_workflow_model_update', '修改', 'auth', 'manage_workflow_model', NULL, 1711, NULL);
CALL add_system_permission('manage_workflow_model_delete', '删除', 'auth', 'manage_workflow_model', NULL, 1712, NULL);
CALL add_system_permission('manage_workflow_model_deploy', '部署', 'auth', 'manage_workflow_model', NULL, 1713, NULL);

CALL add_system_permission('manage_workflow_definition', '部署管理', 'menu', 'manage_workflow', NULL, 172, NULL);
CALL add_system_permission('manage_workflow_definition_update', '修改', 'auth', 'manage_workflow_definition', NULL, 1720, NULL);
CALL add_system_permission('manage_workflow_definition_delete', '删除', 'auth', 'manage_workflow_definition', NULL, 1721, NULL);

CALL add_system_permission('manage_sharding', '分表管理', 'menu', 'manage', 'icon-category-note', 18, NULL);
CALL add_system_permission('manage_sharding_dataSource', '分片数据源配置', 'menu', 'manage_sharding', NULL, 181, NULL);
CALL add_system_permission('manage_sharding_dataSource_read', '查看', 'auth', 'manage_sharding_dataSource', NULL, 1810, NULL);
CALL add_system_permission('manage_sharding_dataSource_add', '新增', 'auth', 'manage_sharding_dataSource', NULL, 1811, NULL);
CALL add_system_permission('manage_sharding_dataSource_delete', '删除', 'auth', 'manage_sharding_dataSource', NULL, 1812, NULL);
CALL add_system_permission('manage_sharding_dataSource_update', '更新', 'auth', 'manage_sharding_dataSource', NULL, 1813, NULL);

CALL add_system_permission('manage_sharding_config', '分片规则配置', 'menu', 'manage_sharding', NULL, 182, NULL);
CALL add_system_permission('manage_sharding_config_read', '查看', 'auth', 'manage_sharding_config', NULL, 1820, NULL);
CALL add_system_permission('manage_sharding_config_add', '新增', 'auth', 'manage_sharding_config', NULL, 1821, NULL);
CALL add_system_permission('manage_sharding_config_update', '更新', 'auth', 'manage_sharding_config', NULL, 1822, NULL);

CALL add_system_permission('manage_sharding_template', '分片模板配置', 'menu', 'manage_sharding', NULL, 183, NULL);
CALL add_system_permission('manage_sharding_template_read', '查看', 'auth', 'manage_sharding_template', NULL, 1830, NULL);
CALL add_system_permission('manage_sharding_template_add', '新增', 'auth', 'manage_sharding_template', NULL, 1831, NULL);
CALL add_system_permission('manage_sharding_template_update', '更新', 'auth', 'manage_sharding_template', NULL, 1832, NULL);

CALL add_system_permission('manage_sharding_algorithm', '分片算法配置', 'menu', 'manage_sharding', NULL, 184, NULL);
CALL add_system_permission('manage_sharding_algorithm_read', '查看', 'auth', 'manage_sharding_algorithm', NULL, 1840, NULL);
CALL add_system_permission('manage_sharding_algorithm_add', '新增', 'auth', 'manage_sharding_algorithm', NULL, 1841, NULL);
CALL add_system_permission('manage_sharding_algorithm_update', '更新', 'auth', 'manage_sharding_algorithm', NULL, 1842, NULL);

CALL add_system_permission('manage_log', '日志管理', 'menu', 'manage', 'task', 19, NULL);
CALL add_system_permission('manage_log_read', '查看', 'auth', 'manage_log', NULL, 191, NULL);

CALL add_system_permission('crm', '客户管理', 'app', NULL, 'icon-Member-management', 2, 'Dynamic');
CALL add_system_permission('crm_customer', '客户管理', 'menu', 'crm', 's-seas', 20, NULL);
CALL add_system_permission('crm_customer_read', '查看列表', 'auth', 'crm_customer', NULL, 201, NULL);
CALL add_system_permission('crm_customer_detail', '查看明细', 'auth', 'crm_customer', NULL, 202, NULL);
CALL add_system_permission('crm_customer_add', '新增', 'auth', 'crm_customer', NULL, 203, NULL);
CALL add_system_permission('crm_customer_delete', '删除', 'auth', 'crm_customer', NULL, 204, NULL);
CALL add_system_permission('crm_customer_update', '更新', 'auth', 'crm_customer', NULL, 205, NULL);
CALL add_system_permission('crm_customer_export', '导出', 'auth', 'crm_customer', NULL, 206, NULL);

CALL add_system_permission('crm_lead', '线索管理', 'menu', 'crm', 'leads', 21, NULL);
CALL add_system_permission('crm_lead_read', '查看列表', 'auth', 'crm_lead', NULL, 211, NULL);
CALL add_system_permission('crm_lead_detail', '查看明细', 'auth', 'crm_lead', NULL, 212, NULL);
CALL add_system_permission('crm_lead_add', '新增', 'auth', 'crm_lead', NULL, 215, NULL);
CALL add_system_permission('crm_lead_delete', '删除', 'auth', 'crm_lead', NULL, 213, NULL);
CALL add_system_permission('crm_lead_update', '更新', 'auth', 'crm_lead', NULL, 214, NULL);
CALL add_system_permission('crm_lead_export', '导出', 'auth', 'crm_lead', NULL, 216, NULL);

CALL add_system_permission('crm_contract', '合同管理', 'menu', 'crm', 'icon-contract', 22, NULL);
CALL add_system_permission('crm_contract_read', '查看列表', 'auth', 'crm_contract', NULL, 221, NULL);
CALL add_system_permission('crm_contract_detail', '查看明细', 'auth', 'crm_contract', NULL, 222, NULL);
CALL add_system_permission('crm_contract_add', '新增', 'auth', 'crm_contract', NULL, 223, NULL);
CALL add_system_permission('crm_contract_delete', '删除', 'auth', 'crm_contract', NULL, 224, NULL);
CALL add_system_permission('crm_contract_update', '更新', 'auth', 'crm_contract', NULL, 225, NULL);
CALL add_system_permission('crm_contract_export', '导出', 'auth', 'crm_contract', NULL, 226, NULL);

CALL add_system_permission('oa', '任务审批', 'app', NULL, 'office', 3, NULL);
CALL add_system_permission('oa_start', '发起审批', 'menu', 'oa', 'top', 30, NULL);
CALL add_system_permission('oa_start_read', '查看列表', 'auth', 'oa_start', NULL, 301, NULL);

CALL add_system_permission('oa_owner', '我的流程', 'menu', 'oa', 'my-task', 31, NULL);
CALL add_system_permission('oa_owner_read', '查看列表', 'auth', 'oa_owner', NULL, 311, NULL);

CALL add_system_permission('oa_undo', '代办任务', 'menu', 'oa', 'contract', 32, NULL);
CALL add_system_permission('oa_undo_read', '查看列表', 'auth', 'oa_undo', NULL, 321, NULL);

CALL add_system_permission('oa_finished', '已办任务', 'menu', 'oa', 'icon-task-state', 33, NULL);
CALL add_system_permission('oa_finished_read', '查看列表', 'auth', 'oa_finished', NULL, 331, NULL);