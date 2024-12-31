package com.lego.core.web;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.feign.CommonService;
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

import java.math.BigDecimal;
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
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        serializeConfig.put(BigDecimal.class, BigDecimalSerializer.instance);
        config.setSerializeConfig(serializeConfig);
        config.setCharset(Constants.DEFAULT_CHARSET);
        config.setSerializerFeatures(getSerializerFeatures());
        converter.setDefaultCharset(Constants.DEFAULT_CHARSET);
        converter.setSupportedMediaTypes(getSupportedMediaTypes());
        return converter;
    }

    public static SerializerFeature[] getSerializerFeatures() {
        return new SerializerFeature[]{
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.PrettyFormat,
            SerializerFeature.DisableCircularReferenceDetect};
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
