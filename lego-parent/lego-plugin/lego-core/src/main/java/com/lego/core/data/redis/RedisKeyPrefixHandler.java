package com.lego.core.data.redis;

import com.lego.core.util.StringUtil;
import org.redisson.api.NameMapper;

/**
 * redis缓存key前缀处理
 */
public class RedisKeyPrefixHandler implements NameMapper {

    private final String keyPrefix;

    public RedisKeyPrefixHandler(String keyPrefix) {
        //前缀为空 则返回空前缀
        this.keyPrefix = StringUtil.isBlank(keyPrefix) ? "" : keyPrefix + ":";
    }

    /**
     * 增加前缀
     */
    @Override
    public String map(String name) {
        if (StringUtil.isBlank(name)) {
            return null;
        }
        if (StringUtil.isNotBlank(keyPrefix) && !name.startsWith(keyPrefix)) {
            return keyPrefix + name;
        }
        return name;
    }

    /**
     * 去除前缀
     */
    @Override
    public String unmap(String name) {
        if (StringUtil.isBlank(name)) {
            return null;
        }
        if (StringUtil.isNotBlank(keyPrefix) && name.startsWith(keyPrefix)) {
            return name.substring(keyPrefix.length());
        }
        return name;
    }

}
