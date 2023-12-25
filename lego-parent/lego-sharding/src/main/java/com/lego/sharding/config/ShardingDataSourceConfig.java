package com.lego.sharding.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.lego.core.data.DataSourceConfig;
import com.lego.core.dto.TypeInfo;
import com.lego.sharding.dto.config.ShardingMetaAlgorithmInfo;
import com.lego.sharding.dto.config.ShardingMetaTableRuleConfigInfo;
import com.lego.sharding.mapper.ShardingAlgorithmMapper;
import com.lego.sharding.mapper.ShardingDataSourceMapper;
import com.lego.sharding.mapper.ShardingPropertiesMapper;
import com.lego.sharding.mapper.ShardingRuleConfigMapper;
import com.lego.sharding.mapper.ShardingTableRuleConfigMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ShardingDataSourceConfig {

	@Autowired
	private DataSourceConfig dataSourceUtil;

	@Value("${sharding.jdbc.show-sql:false}")
	private boolean showSql;

	@Value("${sharding.open:false}")
	private boolean openSharding;

	@Bean
	public DataSource getDataSource() throws Exception {
		DataSource defaultDataSource = dataSourceUtil.createDataSource();
		if (!openSharding) {
			log.info("未开启分表数据源", openSharding);
			return defaultDataSource;
		}
		SqlSession session = metaSqlSessionFactory(defaultDataSource).openSession();
		Set<RuleConfiguration> ruleConfiguration = Collections.singleton(createShardingRuleConfiguration(session));
		Map<String, DataSource> dataSourceMap = createDataSourceMap(session);
		session.close();
		Properties props = new Properties();
		props.put("sql-show", showSql);
		return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfiguration, props);
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

	private ShardingRuleConfiguration createShardingRuleConfiguration(SqlSession session) {
		ShardingRuleConfiguration config = new ShardingRuleConfiguration();
		ShardingRuleConfigMapper ruleConfigMapper = session.getMapper(ShardingRuleConfigMapper.class);
		List<Long> configIds = ruleConfigMapper.selectValid();

		for (Long configId : configIds) {
			createTableRuleConfig(session, config, configId);
			createAlgorithms(session, config, configId);
		}
		config.setDefaultDatabaseShardingStrategy(new NoneShardingStrategyConfiguration());
		config.setDefaultTableShardingStrategy(new NoneShardingStrategyConfiguration());
		config.getKeyGenerators().put("snowflake", new AlgorithmConfiguration("SNOWFLAKE", new Properties()));
		return config;
	}

	private void createTableRuleConfig(SqlSession session, ShardingRuleConfiguration config, Long configId) {
		ShardingTableRuleConfigMapper tableRuleConfigMapper = session.getMapper(ShardingTableRuleConfigMapper.class);
		List<ShardingMetaTableRuleConfigInfo> metaTableRuleConfigs = tableRuleConfigMapper.selectValidBy(configId);

		for (ShardingMetaTableRuleConfigInfo metaTableRuleConfig : metaTableRuleConfigs) {
			log.info("表分片规则：{}", metaTableRuleConfig);
			String templateCode = metaTableRuleConfig.getTemplateCode();
			String algorithmCode = metaTableRuleConfig.getAlgorithmCode();
			String shardingColumn = metaTableRuleConfig.getShardingColumn();
			ShardingTableRuleConfiguration tableRuleConfig = new ShardingTableRuleConfiguration(metaTableRuleConfig.getLogicTableName(), metaTableRuleConfig.getActualDataNodes());
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
			tableRuleConfig.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(shardingColumn, "snowflake"));
			config.getTables().add(tableRuleConfig);
		}
	}

	private void createAlgorithms(SqlSession session, ShardingRuleConfiguration config, Long configId) {
		ShardingAlgorithmMapper algorithmMapper = session.getMapper(ShardingAlgorithmMapper.class);
		List<ShardingMetaAlgorithmInfo> algorithms = algorithmMapper.selectValidBy(configId);
		for (ShardingMetaAlgorithmInfo algorithm : algorithms) {
			log.info("表分片算法：{}", algorithm);
			String code = algorithm.getCode();
			String templateCode = algorithm.getTemplateCode();
			List<TypeInfo> dictionaries = getProperties(session, code, templateCode, algorithm.getConfigCode());
			Properties props = new Properties();
			for (TypeInfo dictionary : dictionaries) {
				log.info("表分片算法配置：{}", dictionary);
				props.setProperty(dictionary.getCode(), dictionary.getName());
			}
			config.getShardingAlgorithms().put(code, new AlgorithmConfiguration(templateCode, props));
		}
	}

	private Map<String, DataSource> createDataSourceMap(SqlSession session) {
		Map<String, DataSource> resluts = new HashMap<String, DataSource>();
		ShardingDataSourceMapper dataSourceMapper = session.getMapper(ShardingDataSourceMapper.class);
		List<TypeInfo> dataSources = dataSourceMapper.selectValid();
		for (TypeInfo dataSource : dataSources) {
			log.info("数据源：{}", dataSource);
			Map<String, Object> dataSourceProps = new HashMap<String, Object>();
			List<TypeInfo> properties = dataSourceMapper.selectValidPropertiesBy(dataSource.getCode());
			for (TypeInfo property : properties) {
				log.info("数据源配置：{}", property);
				dataSourceProps.put(property.getCode(), property.getName());
			}
			resluts.put(dataSource.getCode(), dataSourceUtil.createShardingDataSource(dataSourceProps));
		}
		return resluts;
	}

	private List<TypeInfo> getProperties(SqlSession session, String entityCode, String tmeplateCode, String configCode) {
		ShardingPropertiesMapper metaPropertiesMapper = session.getMapper(ShardingPropertiesMapper.class);
		return metaPropertiesMapper.selectValidBy(entityCode, tmeplateCode, configCode);
	}

}
