package com.lego.core.data;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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

    @Bean
    @ConditionalOnMissingBean
    public DataSource getDataSource() {
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName(className);
        result.setUrl(url);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }

}
