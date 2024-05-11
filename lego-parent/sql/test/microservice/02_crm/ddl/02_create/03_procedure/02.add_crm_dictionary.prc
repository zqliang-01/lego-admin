CREATE PROCEDURE add_crm_dictionary (
    IN vName VARCHAR(255),
    IN vType VARCHAR(255),
    IN vSn INT(5)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vTypeId BIGINT(15);

    SET vId = next_id('general');
    SELECT id INTO vTypeId FROM crm_simple_type t WHERE t.code = vType;

	INSERT INTO crm_dictionaries
	    (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, SERIAL_NUMBER)
	VALUES
	    (vId, vId, vName, 1, sysdate(), vTypeId, vSn);

END;