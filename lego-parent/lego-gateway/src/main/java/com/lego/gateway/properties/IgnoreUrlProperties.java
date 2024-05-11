package com.lego.gateway.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置，跳过Sa登陆校验
 */
@Data
@NoArgsConstructor
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "lego.ignore")
public class IgnoreUrlProperties {

    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private List<String> urls = new ArrayList<>();

}
