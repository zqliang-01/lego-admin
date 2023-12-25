package com.lego;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import com.lego.core.data.hibernate.jpa.RepositoryFactoryBean;
import com.lego.core.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableFeignClients
@SpringBootApplication
@EntityScan("com.lego.**.entity")
@MapperScan({ "com.lego.**.mapper" })
@EnableJpaRepositories(value = "com.lego.**.dao", repositoryFactoryBeanClass = RepositoryFactoryBean.class)
public class LegoAdminApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LegoAdminApplication.class);
    }

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(LegoAdminApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = StringUtil.trim(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n" +
                "Application LegoAdmin is running! Access URLs:\n" +
                "Local: \t\thttp://:" + ip + ":" + port + path + "/\n" +
                "接口文档: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n" +
                "----------------------------------------------------------");
    }
}
