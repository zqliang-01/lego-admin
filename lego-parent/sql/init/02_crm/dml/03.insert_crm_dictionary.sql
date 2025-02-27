INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '介绍', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'leadSource' AND t.class_type = 'SysDictionaryType'), 1, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '电话联系', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'leadSource' AND t.class_type = 'SysDictionaryType'), 2, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '网站搜索', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'leadSource' AND t.class_type = 'SysDictionaryType'), 3, 1);

INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '个人', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'customerType' AND t.class_type = 'SysDictionaryType'), 1, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '企业', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'customerType' AND t.class_type = 'SysDictionaryType'), 2, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '集团', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'customerType' AND t.class_type = 'SysDictionaryType'), 3, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '政府单位', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'customerType' AND t.class_type = 'SysDictionaryType'), 4, 1);

INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '普通', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'contractType' AND t.class_type = 'SysDictionaryType'), 1, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '财务收入', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'contractType' AND t.class_type = 'SysDictionaryType'), 2, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '电子商务', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'contractType' AND t.class_type = 'SysDictionaryType'), 3, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '智慧社区', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'contractType' AND t.class_type = 'SysDictionaryType'), 4, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '代建工程', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'contractType' AND t.class_type = 'SysDictionaryType'), 5, 1);
INSERT INTO sys_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '代理代采', 1, sysdate(),
    (SELECT t.id FROM sys_simple_type t WHERE t.code = 'contractType' AND t.class_type = 'SysDictionaryType'), 6, 1);
