package com.lego.system.config;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import com.lego.core.common.Constants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.ILegoInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
public class SaTokenInterceptor extends SaInterceptor implements ILegoInterceptor {

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public List<String> getPathPatterns() {
        return Arrays.asList("/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkAuth(handler);
        Constants.currentIp.remove();
        Constants.currentIp.set(getIpAddress(request));
        if (StpUtil.isLogin()) {
            Constants.loginCode.remove();
            Constants.loginCode.set(StpUtil.getLoginIdAsString());
        }
        return true;
    }

    private void checkAuth(Object handler) {
        if (isAnnotation && handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();

            // 如果此 Method 或其所属 Class 标注了 @SaIgnore，则忽略掉鉴权
            if (SaStrategy.me.isAnnotationPresent.apply(method, SaIgnore.class)) {
                return;
            }

            if (StpUtil.hasRole(Constants.ADMIN_ROLE_CODE)) {
                return;
            }

            SaStrategy.me.checkElementAnnotation.accept(method.getDeclaringClass());
            SaStrategy.me.checkElementAnnotation.accept(method.getDeclaringClass().getSuperclass());
            SaStrategy.me.checkElementAnnotation.accept(method);
        }
        auth.run(handler);
    }

}
