INSERT INTO crm_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'leadSource', '线索来源', 1, sysdate(), 'CrmDictionaryType', 1);
INSERT INTO crm_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'customerType', '客户类型', 1, sysdate(), 'CrmDictionaryType', 2);
INSERT INTO crm_simple_type (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES (next_id('general'), 'contractType', '合同类型', 1, sysdate(), 'CrmDictionaryType', 3);