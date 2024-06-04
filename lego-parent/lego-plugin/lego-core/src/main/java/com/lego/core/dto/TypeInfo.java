package com.lego.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeInfo extends DTO {

    private static final long serialVersionUID = 75501136291357532L;

    public static final TypeInfo NULL = new TypeInfo();

    private String code;
    private String name;
    private int sn;

    public TypeInfo() {
    }

    public TypeInfo(String code) {
        this.code = code;
    }

    public TypeInfo(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public TypeInfo(String code, String name, int sn) {
        this.code = code;
        this.name = name;
        this.sn = sn;
    }
}
