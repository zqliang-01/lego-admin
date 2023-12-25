INSERT INTO sys_dept
    (ID, CODE, NAME, VERSION, CREATE_TIME, DELETED, ENABLE, PARENT_ID, SERIAL_NUMBER, LEADER_ID)
VALUES
    (next_id('general'), 'system', '系统运维部', 1, sysdate(), 0, 1, NULL, 1, NULL);

-- 密码:hello@1234
INSERT INTO sys_employee
    (ID, CODE, NAME, VERSION, CREATE_TIME, DELETED, PASSWORD, DEPT_ID)
VALUES
    (next_id('general'), 'admin', '超级管理员', 1, sysdate(), 0, '823CB6AB582D3FBA5A4FBB10630A6DAC',
    (SELECT id FROM sys_dept t WHERE t.code = 'system'));

INSERT INTO sys_employee_role
    (EMPLOYEE_ID, ROLE_ID)
VALUES 
    ((select e.id from sys_employee e where e.code = 'admin'), 
    (select r.id from sys_role r where r.code = 'admin'));
