package com.lego.core.module.gen.handler;

import com.lego.core.enums.GenericConditionEnum;
import com.lego.core.vo.GenericConditionItemVO;

import java.util.ArrayList;
import java.util.List;

public interface IFieldTypeHandler {

    String getComponentName();

    Object parseDefaultValue(String value);

    Object parseSearchValue(String value);

    String getJavaFieldType();

    default List<GenericConditionItemVO> buildCondition(GenericConditionEnum condition, List<String> values, String fieldCode) {
        List<GenericConditionItemVO> conditions = new ArrayList<>();
        if (!condition.needValue()) {
            conditions.add(new GenericConditionItemVO(condition, fieldCode));
            return conditions;
        }
        for (String value : values) {
            Object searchValue = parseSearchValue(value);
            if (GenericConditionEnum.LIKE.equals(condition)) {
                searchValue = "%" + searchValue + "%";
            }
            conditions.add(new GenericConditionItemVO(condition, fieldCode, searchValue));
        }
        return conditions;
    };

    boolean matchByType(String type);

    default boolean matchByDataType(String dataTypes) {
        return false;
    };
    default String getTypePackageName() {
        return "";
    };
    default boolean isPublic() {
        return false;
    }
}
