INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '介绍', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'leadSource'), 1, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '电话联系', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'leadSource'), 2, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '网站搜索', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'leadSource'), 3, 1);

INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '个人', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'customerType'), 1, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '企业', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'customerType'), 2, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '集团', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'customerType'), 3, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '政府单位', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'customerType'), 4, 1);

INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '普通', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'contractType'), 1, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '财务收入', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'contractType'), 2, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '电子商务', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'contractType'), 3, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '智慧社区', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'contractType'), 4, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '代建工程', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'contractType'), 5, 1);
INSERT INTO crm_dictionaries (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER, ENABLE)
VALUES (next_id('general'), next_id('general'), '代理代采', 1, sysdate(), (SELECT t.id FROM crm_simple_type t WHERE t.code = 'contractType'), 6, 1);
