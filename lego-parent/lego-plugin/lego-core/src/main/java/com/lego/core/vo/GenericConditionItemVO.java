package com.lego.core.vo;

import com.lego.core.enums.GenericConditionEnum;
import lombok.Getter;
import lombok.Setter;

public class GenericConditionItemVO extends VO {

    private static final long serialVersionUID = 1L;

    private GenericConditionEnum condition;
    @Getter
    private String key;
    @Getter
    private Object value;
    private String conditionKey;
    @Setter
    private int index;

    public GenericConditionItemVO(GenericConditionEnum condition, String key) {
        this(condition, key, null, key);
    }

    public GenericConditionItemVO(GenericConditionEnum condition, String key, Object value) {
        this(condition, key, value, key);
    }

    public GenericConditionItemVO(GenericConditionEnum condition, String key, Object value, String conditionKey) {
        this.condition = condition;
        this.key = key;
        this.value = value;
        this.conditionKey = conditionKey;
    }

    public String getCondition() {
        StringBuilder sb = new StringBuilder("t.");
        sb.append(conditionKey);
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
        return new GenericConditionItemVO(GenericConditionEnum.EQUALS, key, value);
    }

    public static GenericConditionItemVO createEntityEqual(String key, Object value) {
        return new GenericConditionItemVO(GenericConditionEnum.EQUALS, key, value, key + ".code");
    }

    public static GenericConditionItemVO createLike(String key, Object value) {
        return new GenericConditionItemVO(GenericConditionEnum.LIKE, key, value);
    }

}
