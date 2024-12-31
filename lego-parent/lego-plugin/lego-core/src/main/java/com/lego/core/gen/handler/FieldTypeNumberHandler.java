package com.lego.core.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.gen.GenConstants;
import com.lego.core.util.StringUtil;

public class FieldTypeNumberHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldInput";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_NUMBER.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, int.class);
    }

    @Override
    public Object parseSearchValue(String value) {
        return StringUtil.toInt(value);
    }

    @Override
    public String getJavaFieldType() {
        return int.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return "number".equals(type);
    }

}
