package com.lego.core.vo;

import lombok.Getter;

@Getter
public enum SysMessageTypeEnum {

    FLOWABLE("flowable", "审批"),
    FORM("form", "表单");

    private String code;
    private String name;

    private SysMessageTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
