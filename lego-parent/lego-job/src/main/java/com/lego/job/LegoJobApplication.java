package com.lego.job;

import com.lego.core.common.ServiceStartType;
import com.lego.core.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Slf4j
@EnableDiscoveryClient
@ComponentScan({"com.lego"})
@MapperScan({"com.lego.**.mapper"})
@EnableFeignClients({"com.lego.core.feign"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ConditionalOnProperty(name = "lego.start-type", havingValue = ServiceStartType.microservice)
public class LegoJobApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LegoJobApplication.class);
    }

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(LegoJobApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = StringUtil.trim(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n" +
            "Application LegoJob is running! Access URLs:\n" +
            "Local: \t\thttp://:" + ip + ":" + port + path + "/\n" +
            "----------------------------------------------------------");
    }
}
