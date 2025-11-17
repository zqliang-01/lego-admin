create procedure add_system_permission (
    in vcode varchar(255),
    in vname varchar(255),
    in vtypecode varchar(255),
    in vparentcode varchar(255),
    in vicon varchar(255),
    in vsn int(5),
    in vroutetypecode varchar(255)
)
begin
    declare vid bigint(15);
    declare vparentid bigint(15);
    declare vtypeid bigint(15);
    declare vroutetypeid bigint(15);

    set vid = next_id('general');
    if vparentcode is not null then
    	select p.id into vparentid from sys_permission p where p.code = vparentcode;
    end if;
	select t.id into vtypeid from sys_simple_type t where t.code = vtypecode and t.class_type = 'syspermissiontype';

    if vroutetypecode is not null then
	    select t.id into vroutetypeid from sys_simple_type t where t.code = vroutetypecode and t.class_type = 'syspermissionroutetype';
    end if;

	insert into sys_permission
	    (id, code, name, version, create_time, type_id, route_type_id, parent_id, sn, icon)
	values
	    (vid, vcode, vname, 1, sysdate(), vtypeid, vroutetypeid, vparentid, vsn, vicon);

end;