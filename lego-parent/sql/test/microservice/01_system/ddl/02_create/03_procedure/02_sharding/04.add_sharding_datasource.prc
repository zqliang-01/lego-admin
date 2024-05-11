CREATE PROCEDURE add_sharding_datasource (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vDescription VARCHAR(1000)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vTemplateId BIGINT(15);

    SET vId = next_id('general');
    SELECT id INTO vTemplateId FROM sharding_template t WHERE t.code = 'DruidDataSource';

    INSERT INTO sharding_data_source
        (ID, CODE, NAME, DESCRIPTION, TEMPLATE_ID, VERSION, ENABLE, CREATE_TIME)
    VALUES
        (vId, vCode, vName, vDescription, vTemplateId, 1, 1, sysdate());
END;