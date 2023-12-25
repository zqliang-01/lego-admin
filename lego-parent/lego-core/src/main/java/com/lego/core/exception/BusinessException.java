package com.lego.core.exception;

import java.text.MessageFormat;

import com.lego.core.common.ExceptionEnum;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -4175159377328726294L;
    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ExceptionEnum.BUSINESS_INVALID.getCode();
    }

    public BusinessException(String message, Object ... params) {
        this(MessageFormat.format(message, params));
    }

    public BusinessException(ExceptionEnum error) {
        super(error.getMsg());
        this.code = error.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static void check(boolean condition, ExceptionEnum error) {
        if (!condition) {
            throw new BusinessException(error);
        }
    }

    public static void check(boolean condition, String message, Object... params) {
        if (!condition) {
        	if (params == null) {
                throw new BusinessException(message);
        	}
            throw new BusinessException(MessageFormat.format(message, params));
        }
    }
}
