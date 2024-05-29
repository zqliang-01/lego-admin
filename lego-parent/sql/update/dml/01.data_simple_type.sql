
INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'file', '文件', 1, sysdate(), 'DocFileType', 1);

INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'image', '图片', 1, sysdate(), 'DocFileType', 2);

INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'local', '本地', 1, sysdate(), 'DocFileLocation', 1);

INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'aliOss', '阿里云', 1, sysdate(), 'DocFileLocation', 2);

INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'page', '页面', 1, sysdate(), 'DocNodeType', 1);

INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'folder', '目录', 1, sysdate(), 'DocNodeType', 2);

INSERT INTO doc_simple_type
    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
VALUES
    (next_id('general'), 'file', '文件', 1, sysdate(), 'DocNodeType', 3);
