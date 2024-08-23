package com.lego.core.feign;

import cn.dev33.satoken.same.SaSameUtil;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lego.core.common.ExceptionEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.vo.JsonResponse;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig implements RequestInterceptor {

    private static final List<String> NOT_COVERED_HEADERS = Arrays.asList("content-length", "content-type");

    @Autowired
    private FastJsonHttpMessageConverter messageConverter;

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new FeignDecode(feignHttpMessageConverter()));
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        return () -> new HttpMessageConverters(messageConverter);
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

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes != null) {
            // 赋值服务间调用时的请求头参数
            HttpServletRequest request = attributes.getRequest();
            setHeader(requestTemplate, request);
            setCookie(requestTemplate, request.getCookies());
        }
    }

    private void setHeader(RequestTemplate requestTemplate, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (NOT_COVERED_HEADERS.contains(headerName)) {
                    continue;
                }
                String header = request.getHeader(headerName);
                requestTemplate.header(headerName, header);
            }
        }
    }

    public void setCookie(RequestTemplate requestTemplate, Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return;
        }
        for (Cookie cookie : cookies) {
            requestTemplate.header(cookie.getName(), cookie.getValue());
        }
    }

    public class FeignDecode extends SpringDecoder {

        FeignDecode(ObjectFactory<HttpMessageConverters> messageConverters) {
            super(messageConverters);
        }

        @Override
        public Object decode(Response response, Type type) throws FeignException, IOException {
            Object data = super.decode(response, type);
            if (data instanceof JsonResponse) {
                JsonResponse jsonResponse = (JsonResponse) data;
                if (ExceptionEnum.BUSINESS_INVALID.getCode().equals(jsonResponse.getCode())) {
                    throw new BusinessException(jsonResponse.getCode(), jsonResponse.getMsg());
                }
                if (!jsonResponse.isSuccess()) {
                    throw new CoreException(jsonResponse.getCode(), jsonResponse.getMsg());
                }
            }
            return data;
        }
    }

}
