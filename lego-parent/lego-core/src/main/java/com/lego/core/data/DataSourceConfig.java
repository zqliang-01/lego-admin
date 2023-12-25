package com.lego.core.data;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.shardingsphere.infra.datasource.pool.creator.DataSourcePoolCreator;
import org.apache.shardingsphere.infra.datasource.props.DataSourceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfig {

    @Value("${jdbc.classname}")
    private String className;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    public DataSource createDataSource() {
    	DruidDataSource result = new DruidDataSource();
        result.setDriverClassName(className);
        result.setUrl(url);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }

    private static final String DATA_SOURCE_TYPE = "type";

    public DataSource createShardingDataSource(Map<String, Object> dataSourceProps) {
        String type = dataSourceProps.get(DATA_SOURCE_TYPE).toString();
		return DataSourcePoolCreator.create(new DataSourceProperties(type, dataSourceProps));
    }
}
