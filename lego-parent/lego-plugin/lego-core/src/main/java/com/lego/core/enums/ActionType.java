package com.lego.core.enums;

import lombok.Getter;

@Getter
public enum ActionType {

    ADD("add", "新增"),
    MODIFY("modify", "修改"),
    DELETE("delete", "删除"),
    LOGIN("login", "登录"),
    EXPORT("export", "导出");

    private String code;
    private String name;

    private ActionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        for (ActionType value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
