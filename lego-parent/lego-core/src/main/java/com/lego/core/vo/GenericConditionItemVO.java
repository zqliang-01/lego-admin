package com.lego.core.vo;

import lombok.Getter;
import lombok.Setter;

public class GenericConditionItemVO extends VO {

    private static final long serialVersionUID = 1L;

    private CustomFieldTypeEnum fieldType;
    private GenericSearchConditionEnum condition;
    @Getter
    private String key;
    @Getter
    private Object value;
    @Setter
    private int index;

    public GenericConditionItemVO(GenericSearchConditionEnum condition, String key, Object value) {
        this(condition, CustomFieldTypeEnum.TEXT, key, value);
    }

    public GenericConditionItemVO(GenericSearchConditionEnum condition, CustomFieldTypeEnum fieldType, String key, Object value) {
        this.fieldType = fieldType;
        this.condition = condition;
        this.key = key;
        this.value = value;
    }

    public String getCondition() {
        StringBuilder sb = new StringBuilder("t.");
        sb.append(key);
        if (fieldType.isEntity() && condition.needValue()) {
            sb.append(".code");
        }
        sb.append(" ");
        sb.append(condition.getType());
        if (isNeedValue()) {
            sb.append(" :");
            sb.append(getParamKey());
        }
        return sb.toString();
    }

    public String getParamKey() {
        if (index > 0) {
            return key + index;
        }
        return key;
    }

    public boolean isNeedValue() {
        return condition.needValue();
    }

    public static GenericConditionItemVO createEqual(String key, Object value) {
        return new GenericConditionItemVO(GenericSearchConditionEnum.EQUALS, CustomFieldTypeEnum.TEXT, key, value);
    }

    public static GenericConditionItemVO createEqual(CustomFieldTypeEnum fieldType, String key, Object value) {
        return new GenericConditionItemVO(GenericSearchConditionEnum.EQUALS, fieldType, key, value);
    }

    public static GenericConditionItemVO createLike(String key, Object value) {
        return new GenericConditionItemVO(GenericSearchConditionEnum.LIKE, CustomFieldTypeEnum.TEXT, key, value);
    }

}
