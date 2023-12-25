package com.lego.core.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.alibaba.fastjson.JSON;

/** VO的基类 */
public abstract class VO implements Serializable {

    private static final long serialVersionUID = 5946293315696611712L;

    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends VO> T buildAsJson(String json) {
        return (T) JSON.parseObject(json, this.getClass());
    }
}
