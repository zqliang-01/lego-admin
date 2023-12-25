/** =======规则数据====== */
INSERT INTO sharding_config
    (ID, CODE, NAME, DESCRIPTION, VERSION, ENABLE, CREATE_TIME)
VALUES
    (next_id('general'), 'TestRule', '测试规则', NULL, 1, 1, sysdate());

/** =======数据源数据====== */
CALL add_sharding_datasource('sharding', 'DruidDataSource数据源', '阿里DruidDataSource数据源');

/** =======算法规则数据====== */
CALL add_sharding_algorithm('inlineOrder', '行表达式订单表分表算法', 'TestRule', 'INLINE');
CALL add_sharding_algorithm('timeOrder', '时间范围日志表分表算法', 'TestRule', 'INTERVAL');

/** =======分表配置数据====== */
CALL add_sharding_table('InlineOrderTable', '行表达式订单表分表规则', 'res_order', 'sharding.res_order_${[0, 1]}', 'id', 'inlineOrder', 'TestRule', 'standard');
CALL add_sharding_table('InlineLogTable', '行表达式日志表分表规则', 'res_log', 'sharding.res_log_${202304..202306}', 'create_time', 'timeOrder', 'TestRule', 'standard');

/** =======属性数据====== */
CALL add_sharding_algorithm_properties('algorithm-expression', 'res_order_$->{id % 2}', 'inlineOrder', 'TestRule');
CALL add_sharding_algorithm_properties('datetime-pattern', 'yyyy-MM-dd HH:mm:ss', 'timeOrder', 'TestRule');
CALL add_sharding_algorithm_properties('datetime-interval-unit', 'MONTHS', 'timeOrder', 'TestRule');
CALL add_sharding_algorithm_properties('sharding-suffix-pattern', 'yyyyMM', 'timeOrder', 'TestRule');
CALL add_sharding_algorithm_properties('datetime-lower', '2023-01-01 00:00:00', 'timeOrder', 'TestRule');
CALL add_sharding_algorithm_properties('datetime-upper', '2024-01-01 00:00:00', 'timeOrder', 'TestRule');

CALL add_sharding_datasource_properties('password', '123456', 'sharding', 'TestRule');
CALL add_sharding_datasource_properties('driverClassname', 'com.mysql.cj.jdbc.Driver', 'sharding', 'TestRule');
CALL add_sharding_datasource_properties('type', 'com.alibaba.druid.pool.DruidDataSource', 'sharding', 'TestRule');
CALL add_sharding_datasource_properties('url', 'jdbc:mysql://127.0.0.1:3306/lego-admin?useunicode=true&characterEncoding=utf8&serverTimezone=GMT', 'sharding', 'TestRule');
CALL add_sharding_datasource_properties('username', 'root', 'sharding', 'TestRule');
