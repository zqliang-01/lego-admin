CREATE PROCEDURE add_sharding_template (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vTypeCode VARCHAR(255),
    IN vJson TEXT,
    IN vDescription VARCHAR(1000)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vTypeId BIGINT(15);

    SET vId = next_id('general');
    SELECT id INTO vTypeId FROM sharding_template_type t WHERE t.code = vTypeCode;

    INSERT INTO sharding_template
	    (ID, CODE, NAME, JSON, TYPE_ID, VERSION, ENABLE, CREATE_TIME, DESCRIPTION)
	VALUES
	    (vId, vCode, vName, vJson, vTypeId, 1, 1, sysdate(), vDescription);
END;