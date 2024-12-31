package com.lego.core.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.gen.GenConstants;
import com.lego.core.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class FieldTypeTextareaHandler implements IFieldTypeHandler {

    private static final List<String> types = Arrays.asList("textarea");

    @Override
    public String getComponentName() {
        return "FieldTextarea";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_TEXT.contains(dataType);
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
        return "textarea".equals(type);
    }

}
