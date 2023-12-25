
CALL add_system_permission('manage', '管理后台', 'app', NULL, 'all', 1);
CALL add_system_permission('manage:system', '企业首页', 'menu', 'manage', 'enterprise', 10);
CALL add_system_permission('manage:system:read', '查看', 'auth', 'manage:system', NULL, 101);
CALL add_system_permission('manage:system:update', '修改', 'auth', 'manage:system', NULL, 102);

CALL add_system_permission('manage:configSet', '应用管理', 'menu', 'manage', 'all', 11);
CALL add_system_permission('manage:configSet:read', '查看', 'auth', 'manage:configSet', NULL, 111);
CALL add_system_permission('manage:configSet:update', '上下架', 'auth', 'manage:configSet', NULL, 112);

CALL add_system_permission('manage:users', '员工与部门管理', 'menu', 'manage', 's-seas', 12);
CALL add_system_permission('manage:users:read', '查看', 'auth', 'manage:users', NULL, 121);
CALL add_system_permission('manage:users:add', '新增', 'auth', 'manage:users', NULL, 122);
CALL add_system_permission('manage:users:update', '修改', 'auth', 'manage:users', NULL, 123);

CALL add_system_permission('manage:role', '角色权限管理', 'menu', 'manage', 'user', 13);
CALL add_system_permission('manage:role:read', '查看', 'auth', 'manage:role', NULL, 131);
CALL add_system_permission('manage:role:add', '新增', 'auth', 'manage:role', NULL, 132);
CALL add_system_permission('manage:role:update', '修改', 'auth', 'manage:role', NULL, 133);
CALL add_system_permission('manage:role:delete', '删除', 'auth', 'manage:role', NULL, 134);
CALL add_system_permission('manage:role:auth', '授权', 'auth', 'manage:role', NULL, 135);

CALL add_system_permission('manage:genTable', '代码生成', 'menu', 'manage', 'approval-11', 14);
CALL add_system_permission('manage:genTable:read', '查看', 'auth', 'manage:genTable', NULL, 141);
CALL add_system_permission('manage:genTable:sync', '同步', 'auth', 'manage:genTable', NULL, 142);
CALL add_system_permission('manage:genTable:update', '更新', 'auth', 'manage:genTable', NULL, 143);
CALL add_system_permission('manage:genTable:design', '设计', 'auth', 'manage:genTable', NULL, 144);

CALL add_system_permission('manage:customForm', '自定义表单', 'menu', 'manage', 'icon-list', 15);
CALL add_system_permission('manage:customForm:read', '查看', 'auth', 'manage:customForm', NULL, 151);
CALL add_system_permission('manage:customForm:update', '更新', 'auth', 'manage:customForm', NULL, 152);
CALL add_system_permission('manage:customForm:design', '设计', 'auth', 'manage:customForm', NULL, 153);

CALL add_system_permission('manage:sharding', '分表管理', 'menu', 'manage', 'icon-category-note', 16);
CALL add_system_permission('manage:sharding:dataSource', '分片数据源配置', 'menu', 'manage:sharding', NULL, 161);
CALL add_system_permission('manage:sharding:dataSource:read', '查看', 'auth', 'manage:sharding:dataSource', NULL, 1610);
CALL add_system_permission('manage:sharding:dataSource:add', '新增', 'auth', 'manage:sharding:dataSource', NULL, 1611);
CALL add_system_permission('manage:sharding:dataSource:delete', '删除', 'auth', 'manage:sharding:dataSource', NULL, 1612);
CALL add_system_permission('manage:sharding:dataSource:update', '更新', 'auth', 'manage:sharding:dataSource', NULL, 1613);

CALL add_system_permission('manage:sharding:config', '分片规则配置', 'menu', 'manage:sharding', NULL, 162);
CALL add_system_permission('manage:sharding:config:read', '查看', 'auth', 'manage:sharding:config', NULL, 1620);
CALL add_system_permission('manage:sharding:config:add', '新增', 'auth', 'manage:sharding:config', NULL, 1621);
CALL add_system_permission('manage:sharding:config:update', '更新', 'auth', 'manage:sharding:config', NULL, 1622);

CALL add_system_permission('manage:sharding:template', '分片模板配置', 'menu', 'manage:sharding', NULL, 163);
CALL add_system_permission('manage:sharding:template:read', '查看', 'auth', 'manage:sharding:template', NULL, 1630);
CALL add_system_permission('manage:sharding:template:add', '新增', 'auth', 'manage:sharding:template', NULL, 1631);
CALL add_system_permission('manage:sharding:template:update', '更新', 'auth', 'manage:sharding:template', NULL, 1632);

CALL add_system_permission('manage:sharding:algorithm', '分片算法配置', 'menu', 'manage:sharding', NULL, 164);
CALL add_system_permission('manage:sharding:algorithm:read', '查看', 'auth', 'manage:sharding:algorithm', NULL, 1640);
CALL add_system_permission('manage:sharding:algorithm:add', '新增', 'auth', 'manage:sharding:algorithm', NULL, 1641);
CALL add_system_permission('manage:sharding:algorithm:update', '更新', 'auth', 'manage:sharding:algorithm', NULL, 1642);

CALL add_system_permission('manage:sharding:table', '分片表配置', 'menu', 'manage:sharding', NULL, 165);
CALL add_system_permission('manage:sharding:table:read', '查看', 'auth', 'manage:sharding:table', NULL, 1650);
CALL add_system_permission('manage:sharding:table:add', '新增', 'auth', 'manage:sharding:table', NULL, 1651);
CALL add_system_permission('manage:sharding:table:update', '更新', 'auth', 'manage:sharding:table', NULL, 1652);

CALL add_system_permission('crm', '客户管理', 'app', NULL, 'icon-Member-management', 2);
CALL add_system_permission('crm:customer', '客户管理', 'menu', 'crm', 's-seas', 20);
CALL add_system_permission('crm:customer:read', '查看列表', 'auth', 'crm:customer', NULL, 201);
CALL add_system_permission('crm:customer:detail', '查看明细', 'auth', 'crm:customer', NULL, 202);
CALL add_system_permission('crm:customer:add', '新增', 'auth', 'crm:customer', NULL, 203);
CALL add_system_permission('crm:customer:delete', '删除', 'auth', 'crm:customer', NULL, 204);
CALL add_system_permission('crm:customer:update', '更新', 'auth', 'crm:customer', NULL, 205);
CALL add_system_permission('crm:customer:export', '导出', 'auth', 'crm:customer', NULL, 206);

CALL add_system_permission('crm:lead', '线索管理', 'menu', 'crm', 'leads', 21);
CALL add_system_permission('crm:lead:read', '查看列表', 'auth', 'crm:lead', NULL, 211);
CALL add_system_permission('crm:lead:detail', '查看明细', 'auth', 'crm:lead', NULL, 212);
CALL add_system_permission('crm:lead:add', '新增', 'auth', 'crm:lead', NULL, 215);
CALL add_system_permission('crm:lead:delete', '删除', 'auth', 'crm:lead', NULL, 213);
CALL add_system_permission('crm:lead:update', '更新', 'auth', 'crm:lead', NULL, 214);
CALL add_system_permission('crm:lead:export', '导出', 'auth', 'crm:lead', NULL, 216);

CALL add_system_permission('crm:contract', '合同管理', 'menu', 'crm', 'icon-contract', 22);
CALL add_system_permission('crm:contract:read', '查看列表', 'auth', 'crm:contract', NULL, 221);
CALL add_system_permission('crm:contract:detail', '查看明细', 'auth', 'crm:contract', NULL, 222);
CALL add_system_permission('crm:contract:add', '新增', 'auth', 'crm:contract', NULL, 223);
CALL add_system_permission('crm:contract:delete', '删除', 'auth', 'crm:contract', NULL, 224);
CALL add_system_permission('crm:contract:update', '更新', 'auth', 'crm:contract', NULL, 225);
CALL add_system_permission('crm:contract:export', '导出', 'auth', 'crm:contract', NULL, 226);