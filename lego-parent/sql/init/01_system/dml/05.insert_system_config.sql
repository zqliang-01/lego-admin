INSERT INTO sys_config (ID, CODE, NAME, VERSION, CREATE_TIME, VALUE, ENABLE)
VALUES (next_id('general'), 'AppConfig', '系统参数配置', 1, sysdate(), '{"companyLogo":"","companyName":"LegoAdmin"}', 1);

INSERT INTO sys_config (ID, CODE, NAME, VERSION, CREATE_TIME, VALUE, ENABLE)
VALUES (next_id('general'), 'AppValidList', '激活的应用列表', 1, sysdate(), 'oa,manage,crm,report,doc,job', 1);

INSERT INTO sys_config (ID, CODE, NAME, VERSION, CREATE_TIME, VALUE, ENABLE)
VALUES (next_id('general'), 'AppVersion', '系统版本号', 1, sysdate(), 'v1.6.1', 1);
