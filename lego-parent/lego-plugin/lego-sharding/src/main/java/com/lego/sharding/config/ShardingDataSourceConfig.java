package com.lego.sharding.config;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.lego.core.data.DataSourceConfig;
import com.lego.core.data.mybatis.MybatisDynamicExecutor;
import com.lego.core.dto.TypeInfo;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import com.lego.sharding.dto.config.ShardingMetaAlgorithmInfo;
import com.lego.sharding.dto.config.ShardingMetaDataSourceInfo;
import com.lego.sharding.dto.config.ShardingMetaTableInfo;
import com.lego.sharding.mapper.ShardingAlgorithmMapper;
import com.lego.sharding.mapper.ShardingConfigMapper;
import com.lego.sharding.mapper.ShardingDataSourceMapper;
import com.lego.sharding.mapper.ShardingPropertiesMapper;
import com.lego.sharding.mapper.ShardingTableMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.infra.datasource.pool.creator.DataSourcePoolCreator;
import org.apache.shardingsphere.infra.datasource.pool.props.domain.DataSourcePoolProperties;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.single.api.config.SingleRuleConfiguration;
import org.apache.shardingsphere.sql.parser.api.CacheOption;
import org.apache.shardingsphere.sqlfederation.api.config.SQLFederationRuleConfiguration;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
@Configuration
public class ShardingDataSourceConfig {

    private static final String DATA_SOURCE_TYPE = "type";

    @Value("${sharding.jdbc.show-sql:false}")
    private boolean showSql;

    @Value("${sharding.config-code:}")
    private String configCode;

    @Autowired
    private DataSourceConfig dataSourceUtil;

    @Autowired
    private MybatisDynamicExecutor mybatisExecutor;

    @Bean
    @ConditionalOnProperty(name = "sharding.open", havingValue = "true")
    public DataSource getDataSource() throws Exception {
        return createDataSource(configCode);
    }

    public List test(String configCode, String sql) throws Exception {
        DataSource dataSource = createDataSource(configCode);
        SqlSessionFactory sessionFactory = metaSqlSessionFactory(dataSource);
        SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sessionFactory);
        return mybatisExecutor.select(sessionTemplate, sql, new HashMap<>());
    }

    private DataSource createDataSource(String configCode) throws Exception {
        DataSource defaultDataSource = dataSourceUtil.getDataSource();
        SqlSession session = metaSqlSessionFactory(defaultDataSource).openSession();
        Map<String, DataSource> dataSourceMap = createDataSourceMap(session);
        Set<RuleConfiguration> ruleConfiguration = new HashSet<>();
        ruleConfiguration.add(createShardingRuleConfiguration(session, configCode));
        ruleConfiguration.add(createSingleRuleConfiguration(dataSourceMap));
        ruleConfiguration.add(createSQLFederationRuleConfiguration());
        session.close();
        Properties props = new Properties();
        props.put("sql-show", showSql);
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfiguration, props);
    }

    private static SingleRuleConfiguration createSingleRuleConfiguration(Map<String, DataSource> dataSourceMap) {
        SingleRuleConfiguration singleRuleConfiguration = new SingleRuleConfiguration();
        singleRuleConfiguration.setTables(Arrays.asList("*.*"));
        singleRuleConfiguration.setDefaultDataSource(dataSourceMap.keySet().iterator().next());
        return singleRuleConfiguration;
    }

    private SQLFederationRuleConfiguration createSQLFederationRuleConfiguration() {
        CacheOption executionPlanCache = new CacheOption(2000, 65535L);
        return new SQLFederationRuleConfiguration(true, executionPlanCache);
    }

    private SqlSessionFactory metaSqlSessionFactory(DataSource defaultDataSource) throws Exception {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        MybatisConfiguration configuration = new MybatisConfiguration();
        if (showSql) {
            configuration.setLogImpl(StdOutImpl.class);
        }
        configuration.addMappers("com.lego.sharding.mapper");
        Environment environment = new Environment("shardingEvn", new JdbcTransactionFactory(), defaultDataSource);
        configuration.setEnvironment(environment);
        GlobalConfig globalConfig = GlobalConfigUtils.defaults().setBanner(false);
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
        return builder.build(configuration);
    }

    private ShardingRuleConfiguration createShardingRuleConfiguration(SqlSession session, String configCode) {
        ShardingRuleConfiguration config = new ShardingRuleConfiguration();
        ShardingConfigMapper ruleConfigMapper = session.getMapper(ShardingConfigMapper.class);
        List<Long> configIds = ruleConfigMapper.selectValid(configCode);
        CoreException.check(CollectionUtil.isNotEmpty(configIds), "已启用分表数据源但未找到正常可用的分片规则，系统启动失败！", configCode);

        for (Long id : configIds) {
            createTableRuleConfig(session, config, id);
            createAlgorithms(session, config, id);
        }

        config.setDefaultDatabaseShardingStrategy(new NoneShardingStrategyConfiguration());
        config.setDefaultTableShardingStrategy(new NoneShardingStrategyConfiguration());
        config.getKeyGenerators().put("snowflake", new AlgorithmConfiguration("SNOWFLAKE", new Properties()));
        return config;
    }

    private void createTableRuleConfig(SqlSession session, ShardingRuleConfiguration config, Long configId) {
        ShardingTableMapper tableRuleConfigMapper = session.getMapper(ShardingTableMapper.class);
        List<ShardingMetaTableInfo> metaTableRuleConfigs = tableRuleConfigMapper.selectValidBy(configId);

        for (ShardingMetaTableInfo metaTableRuleConfig : metaTableRuleConfigs) {
            log.info("表分片规则：{}", metaTableRuleConfig);
            String templateCode = metaTableRuleConfig.getTemplateCode();
            String algorithmCode = metaTableRuleConfig.getAlgorithmCode();
            String shardingColumn = metaTableRuleConfig.getShardingColumn();
            String dataSourceCode = metaTableRuleConfig.getDataSourceCode();
            String actualTable = metaTableRuleConfig.getActualDataNodes();
            if (StringUtil.isNotBlank(dataSourceCode)) {
                actualTable = dataSourceCode + "." + actualTable;
            }
            ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration(metaTableRuleConfig.getLogicTableName(), actualTable);
            if ("standard".equals(templateCode)) {
                tableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration(shardingColumn, algorithmCode));
            }
            if ("complex".equals(templateCode)) {
                tableRuleConfig.setTableShardingStrategy(new ComplexShardingStrategyConfiguration(shardingColumn, algorithmCode));
            }
            if ("hint".equals(templateCode)) {
                tableRuleConfig.setTableShardingStrategy(new HintShardingStrategyConfiguration(algorithmCode));
            }
            if ("none".equals(templateCode)) {
                tableRuleConfig.setTableShardingStrategy(new NoneShardingStrategyConfiguration());
            }
            if (StringUtil.isNotBlank(shardingColumn)) {
                tableRuleConfig.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(shardingColumn, "snowflake"));
            }
            config.getTables().add(tableRuleConfig);
        }
    }

    private void createAlgorithms(SqlSession session, ShardingRuleConfiguration config, Long configId) {
        ShardingAlgorithmMapper algorithmMapper = session.getMapper(ShardingAlgorithmMapper.class);
        List<ShardingMetaAlgorithmInfo> algorithms = algorithmMapper.selectValidBy(configId);
        for (ShardingMetaAlgorithmInfo algorithm : algorithms) {
            String code = algorithm.getCode();
            String templateCode = algorithm.getTemplateCode();
            List<TypeInfo> dictionaries = getProperties(session, algorithm.getId(), templateCode, algorithm.getConfigCode());
            Properties props = new Properties();
            for (TypeInfo dictionary : dictionaries) {
                props.setProperty(dictionary.getCode(), dictionary.getName());
            }
            config.getShardingAlgorithms().put(code, new AlgorithmConfiguration(templateCode, props));
        }
    }

    private Map<String, DataSource> createDataSourceMap(SqlSession session) throws SQLException {
        Map<String, DataSource> dataSourceMap = new TreeMap<>();
        ShardingDataSourceMapper dataSourceMapper = session.getMapper(ShardingDataSourceMapper.class);
        List<ShardingMetaDataSourceInfo> dataSources = dataSourceMapper.selectValid();
        for (ShardingMetaDataSourceInfo dataSource : dataSources) {
            Map<String, Object> dataSourceProps = new HashMap<String, Object>();
            List<TypeInfo> properties = getProperties(session, dataSource.getId(), null, null);
            for (TypeInfo property : properties) {
                dataSourceProps.put(property.getCode(), property.getName());
            }
            DataSource shardingDataSource = createShardingDataSource(dataSourceProps);
            log.info("数据源装配成功：{}", dataSource.getCode());
            dataSourceMap.put(dataSource.getCode(), shardingDataSource);
        }
        CoreException.check(!dataSourceMap.isEmpty(), "无可用的分区数据源，请配置！");
        return dataSourceMap;
    }

    private List<TypeInfo> getProperties(SqlSession session, Long entityId, String templateCode, String configCode) {
        ShardingPropertiesMapper metaPropertiesMapper = session.getMapper(ShardingPropertiesMapper.class);
        return metaPropertiesMapper.selectValidBy(entityId, templateCode, configCode);
    }

    private DataSource createShardingDataSource(Map<String, Object> dataSourceProps) {
        String type = dataSourceProps.get(DATA_SOURCE_TYPE).toString();
        return DataSourcePoolCreator.create(new DataSourcePoolProperties(type, dataSourceProps));
    }
}
