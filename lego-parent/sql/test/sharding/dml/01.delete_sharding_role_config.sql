DELETE FROM sharding_properties WHERE CONFIG_ID IN (
SELECT c.id FROM sharding_config c WHERE c.CODE = 'TestRule');

DELETE FROM sharding_table WHERE CONFIG_ID IN (
SELECT c.id FROM sharding_config c WHERE c.CODE = 'TestRule');

DELETE FROM sharding_algorithms WHERE CONFIG_ID IN (
SELECT c.id FROM sharding_config c WHERE c.CODE = 'TestRule');

DELETE FROM sharding_data_source WHERE CODE = 'sharding';

DELETE FROM sharding_config WHERE CODE = 'TestRule';
