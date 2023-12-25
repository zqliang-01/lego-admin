package com.lego.pay.feign;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import okhttp3.ConnectionPool;

@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

	@Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
    	MappingJackson2XmlHttpMessageConverter jsonConverter = new MappingJackson2XmlHttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN));
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jsonConverter);
        return () -> httpMessageConverters;
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
        		.readTimeout(60, TimeUnit.SECONDS)
        		.connectTimeout(60, TimeUnit.SECONDS)
        		.writeTimeout(120, TimeUnit.SECONDS)
        		.connectionPool(new ConnectionPool())
        		.build();
    }
}
