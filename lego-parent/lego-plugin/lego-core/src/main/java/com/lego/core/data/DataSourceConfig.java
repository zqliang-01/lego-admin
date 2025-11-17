package com.lego.core.data;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class})
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
    @ConditionalOnProperty(name = "sharding.open", havingValue = "false", matchIfMissing = true)
    public DataSource getDataSource() {
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName(className);
        result.setUrl(url);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }

}
