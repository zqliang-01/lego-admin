package com.lego.core.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.gen.GenConstants;

import java.math.BigDecimal;

public class FieldTypeFloatHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldInput";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_FLOAT.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, BigDecimal.class);
    }

    @Override
    public Object parseSearchValue(String value) {
        return new BigDecimal(value);
    }

    @Override
    public String getJavaFieldType() {
        return BigDecimal.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return "float".equals(type);
    }

    @Override
    public String getTypePackageName() {
        return BigDecimal.class.getName();
    }
}
