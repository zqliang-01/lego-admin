package com.lego.core.module.gen.handler;

import com.alibaba.fastjson2.JSON;
import com.lego.core.module.gen.GenConstants;
import com.lego.core.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class FieldTypeTextHandler implements IFieldTypeHandler {

    private static final List<String> types = Arrays.asList("text", "website", "mobile", "email");

    @Override
    public String getComponentName() {
        return "FieldInput";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_STRING.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, String.class);
    }

    @Override
    public Object parseSearchValue(String value) {
        return StringUtil.trim(value);
    }

    @Override
    public String getJavaFieldType() {
        return String.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return types.contains(type);
    }

}
