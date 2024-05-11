package com.lego.core.exception;

import com.lego.core.common.ExceptionEnum;

import java.text.MessageFormat;

public class CoreException extends RuntimeException {

    private static final long serialVersionUID = 3357231670678262776L;
    private Integer code;

    public CoreException(String message) {
        super(message);
        this.code = ExceptionEnum.UNKNOW_ERROR.getCode();
    }

    public CoreException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CoreException(Throwable e) {
        super(e);
    }

    public CoreException(ExceptionEnum error) {
        super(error.getMsg());
        this.code = error.getCode();
    }

    public CoreException(String message, Throwable e) {
        super(message, e);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static void check(boolean condition, String message, Object... params) {
        if (!condition) {
            throw new CoreException(MessageFormat.format(message, params));
        }
    }
}
