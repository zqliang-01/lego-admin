package com.lego.sharding.config;

import com.lego.core.web.ILegoInterceptor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class ShardingInterceptor implements ILegoInterceptor {

    @Override
    public List<String> getPathPatterns() {
        return Arrays.asList("/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ShardingHintConfig.clear();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ShardingHintConfig.clear();
    }
}
