
create view sys_schema_table as
select * from information_schema.tables;

create view sys_schema_column as
select * from information_schema.columns;

create view sys_schema_db as
select * from information_schema.schemata;