INSERT INTO sharding_template_type
    (ID, CODE, NAME, DESCRIPTION, VERSION, ENABLE, CREATE_TIME)
VALUES
    (next_id('general'), 'DataSource', '数据源', NULL, 1, 1, sysdate());

INSERT INTO sharding_template_type
    (ID, CODE, NAME, DESCRIPTION, VERSION, ENABLE, CREATE_TIME)
VALUES
    (next_id('general'), 'ShardingAlgorithm', '分片算法', NULL, 1, 1, sysdate());

INSERT INTO sharding_template_type
    (ID, CODE, NAME, DESCRIPTION, VERSION, ENABLE, CREATE_TIME)
VALUES
    (next_id('general'), 'ShardingStrategy', '分片策略', NULL, 1, 1, sysdate());
