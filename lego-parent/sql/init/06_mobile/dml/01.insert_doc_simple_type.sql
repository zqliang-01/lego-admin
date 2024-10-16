
INSERT INTO mobile_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'wechat', '微信公众号', 1, sysdate(), 'MobileAppType', 1);
INSERT INTO mobile_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'wechatMp', '微信小程序', 1, sysdate(), 'MobileAppType', 2);
