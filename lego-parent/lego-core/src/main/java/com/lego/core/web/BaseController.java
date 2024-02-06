package com.lego.core.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.common.Constants;
import org.springframework.transaction.annotation.Transactional;

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
        return Constants.loginCode.get();
    }
}
