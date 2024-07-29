package com.lego.gateway.config;

import cn.dev33.satoken.dao.SaTokenDao;
import com.lego.core.web.sa.SaTokenDaoRedisImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("file:public/");
    }

    @Bean
    public SaTokenDao getSaTokenDao() {
        return new SaTokenDaoRedisImpl();
    }
}
