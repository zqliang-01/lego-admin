package com.lego.core.web;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.feign.CommonService;
import com.lego.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LegoWebConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private List<ILegoInterceptor> sessionInterceptes;

    @Value("${sa-token.token-name}")
    private String tokenName;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (sessionInterceptes == null) {
            return;
        }
        for (ILegoInterceptor sessionInterceptor : sessionInterceptes) {
            registry.addInterceptor(sessionInterceptor)
                .addPathPatterns(sessionInterceptor.getPathPatterns())
                .excludePathPatterns(sessionInterceptor.getExcludePathPatterns());
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "DELETE", "PUT")
            .allowedHeaders(tokenName)
            .exposedHeaders(tokenName)
            .maxAge(3600);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, getJsonMessageConverter());
    }

    @Bean
    public FastJsonHttpMessageConverter getJsonMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = converter.getFastJsonConfig();
        config.setWriterFeatures(getSerializerFeatures());
        config.setDateFormat(DateUtil.dateTimePattern);
        config.setCharset(Constants.DEFAULT_CHARSET);
        converter.setDefaultCharset(Constants.DEFAULT_CHARSET);
        converter.setSupportedMediaTypes(getSupportedMediaTypes());
        return converter;
    }

    public static JSONWriter.Feature[] getSerializerFeatures() {
        return new JSONWriter.Feature[]{
            JSONWriter.Feature.WriteNullStringAsEmpty,
            JSONWriter.Feature.WriteNullListAsEmpty,
            JSONWriter.Feature.WriteMapNullValue,
            JSONWriter.Feature.WriteLongAsString,
            JSONWriter.Feature.WriteBigDecimalAsPlain,
            JSONWriter.Feature.PrettyFormat};
    }

    private static List<MediaType> getSupportedMediaTypes() {
        List<MediaType> results = new ArrayList<MediaType>();
        results.add(MediaType.APPLICATION_JSON);
        results.add(MediaType.TEXT_PLAIN);
        results.add(MediaType.parseMediaType("text/json"));
        return results;
    }

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ICommonService getCommonService() {
        return new CommonService();
    }

}
