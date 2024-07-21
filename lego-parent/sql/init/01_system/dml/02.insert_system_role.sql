INSERT INTO sys_role(ID, CODE, NAME, VERSION, CREATE_TIME, DELETED, DATA_SCOPE)
VALUES (next_id('general'), 'admin', '超级管理员', 1, sysdate(), 0, 3);

INSERT INTO sys_role(ID, CODE, NAME, VERSION, CREATE_TIME, DELETED, DATA_SCOPE)
VALUES (next_id('general'), 'TEST001', '体验角色', 1, sysdate(), 0, 3);
