package com.lego.core.enums;

import com.lego.core.gen.handler.*;
import lombok.Getter;

@Getter
public enum FieldTypeEnum {

    TEXT("text", "单行文本", new FieldTypeTextHandler()),
    TEXT_AREA("textarea", "多行文本", new FieldTypeTextareaHandler()),
    BOOLEAN("boolean", "布尔值", new FieldTypeBooleanHandler()),
    NUMBER("number", "整数", new FieldTypeNumberHandler()),
    FLOAT("float", "小数", new FieldTypeFloatHandler()),
    DATE("date", "日期", new FieldTypeDateHandler()),
    DATETIME("datetime", "日期时间", new FieldTypeDateTimeHandler()),
    SELECT("select", "单选", new FieldTypeSelectHandler()),
    EMPLOYEE("user", "员工", new FieldTypeUserHandler()),
    DEPT("structure", "部门", new FieldTypeStructureHandler()),
    ENTITY("entity", "关联表", new FieldTypeEntityHandler()),
    ADDRESS("address", "地址", new FieldTypeAddressHandler());

    private String code;
    private String name;
    private IFieldTypeHandler handler;

    private FieldTypeEnum(String code, String name, IFieldTypeHandler handler) {
        this.code = code;
        this.name = name;
        this.handler = handler;
    }

    public static FieldTypeEnum get(String code) {
        for (FieldTypeEnum value : values()) {
            if (value.handler.matchByType(code)) {
                return value;
            }
        }
        return null;
    }

    public static FieldTypeEnum getByDataType(String dataType) {
        for (FieldTypeEnum value : values()) {
            if (value.getHandler().matchByDataType(dataType)) {
                return value;
            }
        }
        return TEXT;
    }

    public boolean equals(String code) {
        return this.getCode().equals(code);
    }

    public boolean isDate() {
        return this == DATE || this == DATETIME;
    }

    public boolean isPublic() {
        return handler.isPublic();
    }

    public Object parseDefaultValue(String defaultValue) {
        return handler.parseDefaultValue(defaultValue);
    }

    public String getComponentName() {
        return handler.getComponentName();
    }

    public IFieldTypeHandler getHandler() {
        return handler;
    }

    public String getJavaFieldType() {
        return handler.getJavaFieldType();
    }

    public Object parseSearchValue(String value) {
        return handler.parseSearchValue(value);
    }

    public String getTypePackageName() {
        return handler.getTypePackageName();
    }
}
