INSERT INTO sys_dictionaries
SELECT next_id('general') id, t.CODE, t.NAME, t.VERSION, t.CREATE_TIME, st.id type_id, t.SERIAL_NUMBER, t.ENABLE
FROM crm_dictionaries t
JOIN crm_simple_type ct ON ct.id = t.TYPE_ID
JOIN sys_simple_type st ON st.CODE = ct.CODE;