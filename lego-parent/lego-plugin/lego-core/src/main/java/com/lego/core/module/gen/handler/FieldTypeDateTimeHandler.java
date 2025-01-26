package com.lego.core.module.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.module.gen.GenConstants;
import com.lego.core.util.DateUtil;

import java.util.Date;

public class FieldTypeDateTimeHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldInput";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_TIME.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return DateUtil.toDateTimeString(JSON.parseObject(value, Date.class));
    }

    @Override
    public Object parseSearchValue(String value) {
        return DateUtil.toDateTime(value);
    }

    @Override
    public String getJavaFieldType() {
        return Date.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return "datetime".equals(type);
    }

    @Override
    public String getTypePackageName() {
        return Date.class.getName();
    }
}
