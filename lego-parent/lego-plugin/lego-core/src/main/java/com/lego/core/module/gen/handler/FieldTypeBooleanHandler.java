package com.lego.core.module.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.module.gen.GenConstants;

public class FieldTypeBooleanHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldBoolean";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_BOOLEAN.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, boolean.class);
    }

    @Override
    public Object parseSearchValue(String value) {
        return "true".equals(value) ? true : false;
    }

    @Override
    public String getJavaFieldType() {
        return boolean.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return "boolean".equals(type);
    }

}
