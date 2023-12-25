package com.lego.core.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.HandlerInterceptor;

public interface ILegoInterceptor extends HandlerInterceptor {

    default List<String> getExcludePathPatterns() {
        return new ArrayList<String>();
    }

    List<String> getPathPatterns();
}
