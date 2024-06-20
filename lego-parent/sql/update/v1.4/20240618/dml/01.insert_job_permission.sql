
CALL add_system_permission('job', '定时任务', 'app', NULL, 'icon-full-clock', 6, 'local');

CALL add_system_permission('job_index', '运行报表', 'menu', 'job', 'perform', 61, 'local');
CALL add_system_permission('job_index_read', '查看列表', 'auth', 'job_index', NULL, 611, NULL);

CALL add_system_permission('job_task', '任务管理', 'menu', 'job', 'icon-full-setting', 62, 'local');
CALL add_system_permission('job_task_read', '查看列表', 'auth', 'job_task', NULL, 621, NULL);
CALL add_system_permission('job_task_add', '新增', 'auth', 'job_task', NULL, 622, NULL);
CALL add_system_permission('job_task_delete', '删除', 'auth', 'job_task', NULL, 623, NULL);
CALL add_system_permission('job_task_update', '更新', 'auth', 'job_task', NULL, 624, NULL);

CALL add_system_permission('job_log', '调度日志', 'menu', 'job', 'plan', 63, 'local');
CALL add_system_permission('job_log_read', '查看列表', 'auth', 'job_log', NULL, 631, NULL);
CALL add_system_permission('job_log_delete', '删除', 'auth', 'job_log', NULL, 632, NULL);

CALL add_system_permission('job_executor', '执行器管理', 'menu', 'job', 'my-task', 64, 'local');
CALL add_system_permission('job_executor_read', '查看列表', 'auth', 'job_executor', NULL, 641, NULL);
CALL add_system_permission('job_executor_add', '新增', 'auth', 'job_executor', NULL, 642, NULL);
CALL add_system_permission('job_executor_delete', '删除', 'auth', 'job_executor', NULL, 643, NULL);
CALL add_system_permission('job_executor_update', '更新', 'auth', 'job_executor', NULL, 644, NULL);