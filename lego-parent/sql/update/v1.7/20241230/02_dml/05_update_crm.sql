
UPDATE crm_contract t SET t.TYPE =
(SELECT d.code from crm_dictionaries d WHERE d.ID = t.TYPE_ID)
WHERE EXISTS (SELECT d.code from crm_dictionaries d WHERE d.ID = t.TYPE_ID);

UPDATE crm_customer t SET t.TYPE =
(SELECT d.code from crm_dictionaries d WHERE d.ID = t.TYPE_ID)
WHERE EXISTS (SELECT d.code from crm_dictionaries d WHERE d.ID = t.TYPE_ID);

UPDATE crm_lead t SET t.SOURCE =
(SELECT d.code from crm_dictionaries d WHERE d.ID = t.SOURCE_ID)
WHERE EXISTS (SELECT d.code from crm_dictionaries d WHERE d.ID = t.SOURCE_ID);