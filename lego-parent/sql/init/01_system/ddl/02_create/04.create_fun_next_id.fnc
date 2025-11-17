
create function next_id(vname varchar(50)) returns bigint(15)
deterministic
begin
	declare vvalue bigint;

	if vname is null or vname = '' then
		set vname = 'general';
	end if;

	update sys_sequence set id = last_insert_id(id + 1)
	where  name = vname;
	select last_insert_id() into vvalue from sys_sequence where name = vname;

	return vvalue;
end;