CREATE PROCEDURE add_system_simple_type (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vClassType VARCHAR(255),
    IN vSn INT(5)
)
BEGIN
    DECLARE vId BIGINT(15);

    SET vId = next_id('general');

	INSERT INTO sys_simple_type
	    (ID, CODE, NAME, VERSION, CREATE_TIME, CLASS_TYPE, SERIAL_NUMBER)
	VALUES
	    (vId, vCode, vName, 1, sysdate(), vClassType, vSn);

END;