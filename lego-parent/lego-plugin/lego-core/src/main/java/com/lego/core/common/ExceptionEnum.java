package com.lego.core.common;

public enum ExceptionEnum {
    SUCCESS(200, "成功"),
    UNKNOW_ERROR(9999, "未知异常"),
    SQL_ERROR(6000, "数据脚本异常"),
    BUSINESS_INVALID(3000, "业务异常"),
    TOKEN_INVALID(4000, "Token校验不通过"),
    AUTHORIZATION_INVALID(5000, "功能未授权"),
    METHOD_INVALID(5001, "请求方法类型错误"),
    PARAM_INVALID(5002, "请求参数类型错误"),
    SESSION_INVALID(1000, "登陆超时");

    private Integer code;
    private String msg;

    private ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static String getMsgBy(Integer code) {
        for (ExceptionEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item.getMsg();
            }
        }
        return UNKNOW_ERROR.getMsg();
    }
}
