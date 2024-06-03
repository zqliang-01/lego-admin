package com.lego.core.vo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * VO的基类
 */
public abstract class VO implements Serializable {

    private static final long serialVersionUID = 5946293315696611712L;

    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends VO> T buildAsJson(String json) {
        return (T) JSON.parseObject(json, this.getClass());
    }
}
