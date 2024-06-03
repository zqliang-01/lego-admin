package com.lego.core.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.lego.core.common.ExceptionEnum;
import lombok.Data;

import java.util.ArrayList;

@Data
public class JsonResponse<T> {

    /**
     * 状态码
     **/
    private Integer code;

    /**
     * 提示信息
     **/
    private String msg;

    /**
     * 响应数据
     **/
    private T data;

    /**
     * 无参构造
     **/
    protected JsonResponse() {
    }

    /**
     * 带参构造
     */
    public JsonResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static JsonResponse<Object> success() {
        return new JsonResponse<>(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMsg(), new ArrayList<>());
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<>(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMsg(), data);
    }

    public static JsonResponse<Object> success(Integer code, String msg) {
        return new JsonResponse<>(code, msg, new ArrayList<>());
    }

    public static <T> JsonResponse<T> success(String msg, T data) {
        return new JsonResponse<>(ExceptionEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> JsonResponse<T> success(Integer code, String msg, T data) {
        return new JsonResponse<>(code, msg, data);
    }

    /**
     * 响应失败结果
     */
    public static JsonResponse<Object> failed(String msg) {
        return new JsonResponse<>(ExceptionEnum.UNKNOW_ERROR.getCode(), msg, new ArrayList<>());
    }

    public static <T> JsonResponse<T> failed(T data) {
        return new JsonResponse<T>(ExceptionEnum.UNKNOW_ERROR.getCode(), ExceptionEnum.UNKNOW_ERROR.getMsg(), data);
    }

    public static JsonResponse<Object> failed(ExceptionEnum exceptionEnum) {
        return new JsonResponse<>(exceptionEnum.getCode(), exceptionEnum.getMsg(), new ArrayList<>());
    }

    public static JsonResponse<Object> failed(Integer code, String msg) {
        return new JsonResponse<>(code, msg, new ArrayList<>());
    }

    public static <T> JsonResponse<T> failed(Integer code, String msg, T data) {
        return new JsonResponse<>(code, msg, data);
    }

    @JSONField(serialize = false)
    public boolean isSuccess() {
        return ExceptionEnum.SUCCESS.getCode().equals(code);
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
