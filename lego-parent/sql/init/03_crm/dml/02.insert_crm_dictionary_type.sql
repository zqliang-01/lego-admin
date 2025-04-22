
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'leadSource', '线索来源', 1, sysdate(), 'SysDictionaryType', 1);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'customerType', '客户类型', 1, sysdate(), 'SysDictionaryType', 2);
INSERT INTO sys_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'contractType', '合同类型', 1, sysdate(), 'SysDictionaryType', 3);