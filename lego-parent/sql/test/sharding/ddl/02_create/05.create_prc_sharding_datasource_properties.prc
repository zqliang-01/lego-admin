CREATE PROCEDURE add_sharding_datasource_properties (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vEntityCode VARCHAR(255),
    IN vConfigCode VARCHAR(255)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vConfigId BIGINT(15);
    DECLARE vObjectId BIGINT(15);
    DECLARE vTemplateId BIGINT(15);
    DECLARE vEntityId BIGINT(15);

    SET vId = next_id('general');
    SELECT id INTO vConfigId FROM sharding_config t WHERE t.code = vConfigCode;
    SELECT template_id INTO vTemplateId FROM sharding_data_source a WHERE a.code = vEntityCode;
    SELECT id INTO vEntityId FROM sharding_data_source a WHERE a.code = vEntityCode;

	INSERT INTO sharding_properties
	    (ID, CODE, NAME, DESCRIPTION, CONFIG_ID, ENTITY_ID, VERSION, ENABLE, CREATE_TIME, TEMPLATE_ID)
	VALUES
	    (vId, vCode, vName, NULL, vConfigId, vEntityId, 1, 1, sysdate(), vTemplateId);

END;