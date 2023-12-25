CREATE PROCEDURE add_system_config (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vValue VARCHAR(1000)
)
BEGIN
    DECLARE vId BIGINT(15);
    SET vId = next_id('general');

	INSERT INTO sys_config
	    (ID, CODE, NAME, VERSION, CREATE_TIME, VALUE, ENABLE)
	VALUES
	    (vId, vCode, vName, 1, sysdate(), vValue, 1);

END;