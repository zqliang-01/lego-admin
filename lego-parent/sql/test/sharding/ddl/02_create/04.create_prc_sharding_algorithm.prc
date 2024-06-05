CREATE PROCEDURE add_sharding_algorithm (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vConfigCode VARCHAR(255),
    IN vTemplateCode VARCHAR(255)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vConfigId BIGINT(15);
    DECLARE vTemplateId BIGINT(15);

    SET vId = next_id('general');
    SELECT id INTO vConfigId FROM sharding_config t WHERE t.code = vConfigCode;
    SELECT id INTO vTemplateId FROM sharding_template t WHERE t.code = vTemplateCode;

    INSERT INTO sharding_algorithms
        (ID, CODE, NAME, DESCRIPTION, TEMPLATE_ID, CONFIG_ID, VERSION, ENABLE, CREATE_TIME)
    VALUES
        (vId, vCode, vName, NULL, vTemplateId, vConfigId, 1, 1, sysdate());
END;