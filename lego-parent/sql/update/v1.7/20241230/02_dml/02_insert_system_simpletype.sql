INSERT INTO sys_simple_type
SELECT next_id('general') id, t.CODE, t.NAME, t.VERSION, t.CREATE_TIME, 'SysDictionaryType', t.SERIAL_NUMBER
FROM crm_simple_type t;