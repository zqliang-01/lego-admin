package com.lego.core.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.data.hibernate.entity.BusEntity;
import com.lego.core.enums.GenericConditionEnum;
import com.lego.core.gen.GenConstants;
import com.lego.core.vo.GenericConditionItemVO;

import java.util.ArrayList;
import java.util.List;

public class FieldTypeEntityHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldInput";
    }

    @Override
    public boolean matchByDataType(String dataType) {
        return GenConstants.COLUMNTYPE_REFERENCE_ENTITY.contains(dataType);
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, String.class);
    }

    @Override
    public Object parseSearchValue(String value) {
        return value;
    }

    @Override
    public String getJavaFieldType() {
        return BusEntity.class.getSimpleName();
    }

    @Override
    public List<GenericConditionItemVO> buildCondition(GenericConditionEnum condition, List<String> values, String fieldCode) {
        List<GenericConditionItemVO> conditions = new ArrayList<>();
        if (!condition.needValue()) {
            conditions.add(new GenericConditionItemVO(condition, fieldCode));
            return conditions;
        }
        for (String value : values) {
            Object searchValue = parseSearchValue(value);
            conditions.add(new GenericConditionItemVO(condition, fieldCode, searchValue, fieldCode + ".code"));
        }
        return conditions;
    }

    @Override
    public boolean matchByType(String type) {
        return "entity".equals(type);
    }

}
