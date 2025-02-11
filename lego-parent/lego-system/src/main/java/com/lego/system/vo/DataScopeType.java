package com.lego.system.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataScopeType {

    SELF(0, "本人"),
    DEPT(1, "本部门"),
    DEPT_AND_CHILD(2, "本部门及下级部门"),
    ALL(3, "全部");

    private int code;
    private String name;

    public static DataScopeType get(int code) {
        for (DataScopeType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static String getName(int code) {
        DataScopeType type = get(code);
        if (type != null) {
            return type.name;
        }
        return "";
    }
}
