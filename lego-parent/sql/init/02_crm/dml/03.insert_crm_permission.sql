
CALL add_system_permission('crm', '客户管理', 'app', NULL, 'icon-Member-management', 2, 'dynamic');
CALL add_system_permission('crm_customer', '客户管理', 'menu', 'crm', 's-seas', 20, 'Dynamic');
CALL add_system_permission('crm_customer_read', '查看列表', 'auth', 'crm_customer', NULL, 201, NULL);
CALL add_system_permission('crm_customer_detail', '查看明细', 'auth', 'crm_customer', NULL, 202, NULL);
CALL add_system_permission('crm_customer_add', '新增', 'auth', 'crm_customer', NULL, 203, NULL);
CALL add_system_permission('crm_customer_delete', '删除', 'auth', 'crm_customer', NULL, 204, NULL);
CALL add_system_permission('crm_customer_update', '更新', 'auth', 'crm_customer', NULL, 205, NULL);
CALL add_system_permission('crm_customer_export', '导出', 'auth', 'crm_customer', NULL, 206, NULL);

CALL add_system_permission('crm_lead', '线索管理', 'menu', 'crm', 'leads', 21, 'dynamic');
CALL add_system_permission('crm_lead_read', '查看列表', 'auth', 'crm_lead', NULL, 211, NULL);
CALL add_system_permission('crm_lead_detail', '查看明细', 'auth', 'crm_lead', NULL, 212, NULL);
CALL add_system_permission('crm_lead_add', '新增', 'auth', 'crm_lead', NULL, 215, NULL);
CALL add_system_permission('crm_lead_delete', '删除', 'auth', 'crm_lead', NULL, 213, NULL);
CALL add_system_permission('crm_lead_update', '更新', 'auth', 'crm_lead', NULL, 214, NULL);
CALL add_system_permission('crm_lead_export', '导出', 'auth', 'crm_lead', NULL, 216, NULL);

CALL add_system_permission('crm_contract', '合同管理', 'menu', 'crm', 'contract', 22, 'dynamic');
CALL add_system_permission('crm_contract_read', '查看列表', 'auth', 'crm_contract', NULL, 221, NULL);
CALL add_system_permission('crm_contract_detail', '查看明细', 'auth', 'crm_contract', NULL, 222, NULL);
CALL add_system_permission('crm_contract_add', '新增', 'auth', 'crm_contract', NULL, 223, NULL);
CALL add_system_permission('crm_contract_delete', '删除', 'auth', 'crm_contract', NULL, 224, NULL);
CALL add_system_permission('crm_contract_update', '更新', 'auth', 'crm_contract', NULL, 225, NULL);
CALL add_system_permission('crm_contract_export', '导出', 'auth', 'crm_contract', NULL, 226, NULL);