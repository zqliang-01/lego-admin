package com.lego.gateway;

import com.lego.core.common.ServiceStartType;
import com.lego.core.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@ConditionalOnProperty(name = "lego.start-type", havingValue = ServiceStartType.microservice)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LegoGatewayApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(LegoGatewayApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = StringUtil.trim(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n" +
            "Application LegoGateway is running! Access URLs:\n" +
            "Local: \t\thttp://:" + ip + ":" + port + path + "/\n" +
            "----------------------------------------------------------");
    }
}
