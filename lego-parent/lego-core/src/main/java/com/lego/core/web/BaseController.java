package com.lego.core.web;

import org.springframework.transaction.annotation.Transactional;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;

@SaCheckLogin
@Transactional
public abstract class BaseController {

    @SuppressWarnings("unchecked")
    protected <T> T getAttribute(String key) {
        return (T) StpUtil.getSession().get(key);
    }

    protected void setAttribute(String key, Object value) {
        StpUtil.getSession().set(key, value);
    }

    protected String getLoginCode() {
    	return StpUtil.getLoginIdAsString();
    }
}
