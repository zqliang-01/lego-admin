package com.lego.core.web;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ILegoInterceptor extends HandlerInterceptor {

    default List<String> getExcludePathPatterns() {
        return new ArrayList<String>();
    }

    default List<String> getPathPatterns() {
        return Arrays.asList("/**");
    }
}
