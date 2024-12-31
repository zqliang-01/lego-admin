package com.lego.core.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.gen.GenConstants;
import com.lego.core.util.DateUtil;

import java.util.Date;

public class FieldTypeDateHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldInput";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_DATE.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return DateUtil.toDateString(JSON.parseObject(value, Date.class));
    }

    @Override
    public Object parseSearchValue(String value) {
        return DateUtil.toDate(value);
    }

    @Override
    public String getJavaFieldType() {
        return Date.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return "date".equals(type);
    }

    @Override
    public String getTypePackageName() {
        return Date.class.getName();
    }
}
