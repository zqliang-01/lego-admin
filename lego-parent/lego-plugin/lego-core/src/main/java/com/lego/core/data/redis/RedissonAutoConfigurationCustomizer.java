package com.lego.core.data.redis;

import org.redisson.config.Config;

@FunctionalInterface
public interface RedissonAutoConfigurationCustomizer {

    void customize(final Config configuration);
}
