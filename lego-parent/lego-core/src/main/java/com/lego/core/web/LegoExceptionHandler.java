package com.lego.core.web;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lego.core.common.Constants;
import com.lego.core.common.ExceptionEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.vo.JsonResponse;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class LegoExceptionHandler {

    @Autowired
    protected FastJsonHttpMessageConverter messageConverter;

    @Autowired(required = false)
    private List<ILegoExceptionHandler> handlers;

    @ExceptionHandler(Throwable.class)
    public ModelAndView defaultErrorHandler(HttpServletResponse response, Throwable e) throws IOException {
        String errorMsg = e.getMessage();
        Integer errorCode = ExceptionEnum.UNKNOW_ERROR.getCode();
        if (CollectionUtil.isNotEmpty(handlers)) {
            for (ILegoExceptionHandler handler : handlers) {
                if (handler.accept(e)) {
                    errorCode = handler.getCode(e);
                    errorMsg = handler.getMessage(e);
                }
            }
        }
        if (e instanceof SQLException
            || e instanceof PersistenceException
            || e instanceof DataAccessException) {
            errorMsg = ExceptionEnum.SQL_ERROR.getMsg();
            errorCode = ExceptionEnum.SQL_ERROR.getCode();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            errorMsg = ExceptionEnum.METHOD_INVALID.getMsg();
            errorCode = ExceptionEnum.METHOD_INVALID.getCode();
        } else if (e instanceof HttpMessageNotReadableException) {
            errorMsg = ExceptionEnum.PARAM_INVALID.getMsg();
            errorCode = ExceptionEnum.PARAM_INVALID.getCode();
        } else if (e instanceof CoreException) {
            CoreException coreException = (CoreException) e;
            errorMsg = coreException.getMessage();
            errorCode = coreException.getCode();
            e = getRootCause(e);
        } else {
            errorMsg = "全局未知异常，请联系技术人员处理！";
        }
        log.error(errorMsg, e);
        return handlerResponse(response, errorMsg, errorCode);
    }

    @ExceptionHandler(DecodeException.class)
    public ModelAndView loginError(HttpServletResponse response, DecodeException e) throws IOException {
        Throwable root = getRootCause(e);
        if (root instanceof BusinessException) {
            BusinessException bx = (BusinessException) root;
            return businessErrorHandler(response, bx);
        }
        return defaultErrorHandler(response, root);
    }

    @ExceptionHandler(NotPermissionException.class)
    public ModelAndView permissionError(HttpServletResponse response, NotPermissionException e) throws IOException {
        Integer errorCode = ExceptionEnum.AUTHORIZATION_INVALID.getCode();
        String errorMsg = ExceptionEnum.AUTHORIZATION_INVALID.getMsg();
        log.error(e.getMessage());
        return handlerResponse(response, errorMsg, errorCode);
    }

    @ExceptionHandler(NotLoginException.class)
    public ModelAndView loginError(HttpServletResponse response, NotLoginException e) throws IOException {
        Integer errorCode = ExceptionEnum.SESSION_INVALID.getCode();
        String errorMsg = ExceptionEnum.SESSION_INVALID.getMsg();
        return handlerResponse(response, errorMsg, errorCode);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ModelAndView businessErrorHandler(HttpServletResponse response, BusinessException e) throws IOException {
        Integer errorCode = e.getCode();
        String errorMsg = e.getMessage();
        return handlerResponse(response, errorMsg, errorCode);
    }

    protected ModelAndView handlerResponse(HttpServletResponse response, String errorMsg, Integer errorCode) throws IOException {
        response.setContentType(Constants.JSON_MEDIA_TYPE_NAME);
        response.setCharacterEncoding(Constants.DEFAULT_CHARSET_NAME);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = null;
        try {
            JsonResponse<Object> result = JsonResponse.failed(errorCode, errorMsg);
            messageConverter.write(result, Constants.JSON_MEDIA_TYPE, new ServletServerHttpResponse(response));
        } catch (Throwable ex) {
            JsonResponse<Object> result = JsonResponse.failed(errorCode, errorMsg);
            writer = response.getWriter();
            writer.println(JSON.toJSONString(result));
            log.error("输出异常返回结果时发生错误:", ex);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return new ModelAndView();
    }

    public Throwable getRootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root;
    }
}
