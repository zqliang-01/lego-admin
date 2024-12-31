package com.lego.core.gen.handler;

import com.alibaba.fastjson.JSON;
import com.lego.core.data.hibernate.entity.AddressEntity;
import com.lego.core.enums.GenericConditionEnum;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.GenericConditionItemVO;

import java.util.ArrayList;
import java.util.List;

public class FieldTypeAddressHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldAddress";
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, AddressEntity.class);
    }

    @Override
    public AddressEntity parseSearchValue(String value) {
        return JSON.parseObject(value, AddressEntity.class);
    }

    @Override
    public String getJavaFieldType() {
        return this.getTypePackageName();
    }

    @Override
    public List<GenericConditionItemVO> buildCondition(GenericConditionEnum condition, List<String> values, String fieldCode) {
        List<GenericConditionItemVO> conditions = new ArrayList<>();
        for (String value : values) {
            AddressEntity searchValue = parseSearchValue(value);
            if (StringUtil.isNotBlank(searchValue.getProvince())) {
                conditions.add(new GenericConditionItemVO(condition, "province", searchValue.getProvince(), fieldCode + ".province"));
            }
            if (StringUtil.isNotBlank(searchValue.getCity())) {
                conditions.add(new GenericConditionItemVO(condition, "city", searchValue.getCity(), fieldCode + ".city"));
            }
            if (StringUtil.isNotBlank(searchValue.getArea())) {
                conditions.add(new GenericConditionItemVO(condition, "area", searchValue.getArea(), fieldCode + ".area"));
            }
            if (StringUtil.isNotBlank(searchValue.getDetail())) {
                conditions.add(new GenericConditionItemVO(condition, "detail", searchValue.getDetail(), fieldCode + ".detail"));
            }
        }
        return conditions;
    }

    @Override
    public boolean matchByType(String type) {
        return "address".equals(type);
    }

    @Override
    public String getTypePackageName() {
        return AddressEntity.class.getName();
    }
}
