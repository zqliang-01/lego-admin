package com.lego.core.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {

    FLOWABLE("flowable", "审批"),
    FORM("form", "表单");

    private String code;
    private String name;

    private MessageTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
