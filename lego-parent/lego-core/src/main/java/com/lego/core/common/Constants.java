package com.lego.core.common;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;

public interface Constants {
    String DEFAULT_LANG = "zh";
    String ADMIN_ROLE = "admin";
    String SEPARATOR =",";

    String DEFAULT_CHARSET_NAME = "UTF-8";
    Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);

    String JSON_MEDIA_TYPE_NAME = "application/json;charset=UTF-8";
    MediaType JSON_MEDIA_TYPE = new MediaType("application", "json", DEFAULT_CHARSET);

    ThreadLocal<String> currentIp = new ThreadLocal<String>();
}
