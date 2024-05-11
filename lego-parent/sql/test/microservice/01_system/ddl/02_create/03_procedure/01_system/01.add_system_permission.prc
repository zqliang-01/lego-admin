CREATE PROCEDURE add_system_permission (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vTypeCode VARCHAR(255),
    IN vParentCode VARCHAR(255),
    IN vIcon VARCHAR(255),
    IN vSn INT(5),
    IN vRouteTypeCode VARCHAR(255)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vParentId BIGINT(15);
    DECLARE vTypeId BIGINT(15);
    DECLARE vRouteTypeId BIGINT(15);

    SET vId = next_id('general');
    IF vParentCode IS NOT NULL THEN
    	SELECT p.id INTO vParentId FROM sys_permission p WHERE p.code = vParentCode;
    END IF;
	SELECT t.id INTO vTypeId FROM sys_simple_type t WHERE t.code = vTypeCode AND t.class_type = 'SysPermissionType';

    IF vRouteTypeCode IS NOT NULL THEN
	    SELECT t.id INTO vRouteTypeId FROM sys_simple_type t WHERE t.code = vRouteTypeCode AND t.class_type = 'SysPermissionRouteType';
    END IF;

	INSERT INTO sys_permission
	    (ID, CODE, NAME, VERSION, CREATE_TIME, TYPE_ID, ROUTE_TYPE_ID, PARENT_ID, SN, ICON)
	VALUES
	    (vId, vCode, vName, 1, sysdate(), vTypeId, vRouteTypeId, vParentId, vSn, vIcon);

END;