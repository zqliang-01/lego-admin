package com.lego.core.common;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

public interface Constants {

    String DEFAULT_LANG = "zh";
    String ADMIN_ROLE = "admin";
    String SEPARATOR = ",";

    String DEFAULT_CHARSET_NAME = "UTF-8";
    Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    MediaType JSON_MEDIA_TYPE = new MediaType("application", "json", DEFAULT_CHARSET);
    String JSON_MEDIA_TYPE_NAME = "application/json;charset=UTF-8";
    ThreadLocal<String> currentIp = new ThreadLocal<String>();
    ThreadLocal<String> loginCode = new ThreadLocal<String>();
}
