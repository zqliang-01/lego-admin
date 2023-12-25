package com.lego.system.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.lego.core.common.Constants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.ILegoInterceptor;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;

@Component
public class SaTokenInterceptor extends SaInterceptor implements ILegoInterceptor {

	@Override
	public List<String> getPathPatterns() {
		return Arrays.asList("/**");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (isAnnotation && handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();

			// 如果此 Method 或其所属 Class 标注了 @SaIgnore，则忽略掉鉴权
			if (SaStrategy.me.isAnnotationPresent.apply(method, SaIgnore.class)) {
				return true;
			}

			if (StpUtil.hasRole(Constants.ADMIN_ROLE)) {
				return true;
			}

			SaStrategy.me.checkElementAnnotation.accept(method.getDeclaringClass());
			SaStrategy.me.checkElementAnnotation.accept(method.getDeclaringClass().getSuperclass());
			SaStrategy.me.checkElementAnnotation.accept(method);
		}
		auth.run(handler);
		Constants.currentIp.remove();
		Constants.currentIp.set(getIpAddress(request));
		return true;
	}

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
}
