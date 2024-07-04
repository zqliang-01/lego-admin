
CREATE VIEW sys_schema_table AS
SELECT * FROM information_schema.tables;

CREATE VIEW sys_schema_column AS
SELECT * FROM information_schema.columns;

CREATE VIEW sys_schema_db AS
SELECT * FROM information_schema.schemata;