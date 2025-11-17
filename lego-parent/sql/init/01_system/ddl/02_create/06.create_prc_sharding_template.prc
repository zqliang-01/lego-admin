create procedure add_sharding_template (
    in vcode varchar(255),
    in vname varchar(255),
    in vtypecode varchar(255),
    in vjson text,
    in vdescription varchar(1000)
)
begin
    declare vid bigint(15);
    declare vtypeid bigint(15);

    set vid = next_id('general');
    select id into vtypeid from sharding_template_type t where t.code = vtypecode;

    insert into sharding_template
	    (id, code, name, json, type_id, version, enable, create_time, description)
	values
	    (vid, vcode, vname, vjson, vtypeid, 1, 1, sysdate(), vdescription);
end;