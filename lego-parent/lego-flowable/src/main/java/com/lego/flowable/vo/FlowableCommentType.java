package com.lego.flowable.vo;

import com.lego.core.dto.TypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowableCommentType {

    GENERIC("generic", "通过"),
    DELEGATE("delegate", "委派"),
    TRANSFER("transfer", "转办"),
    REJECT("reject", "拒绝");

    private final String code;
    private final String name;

    public static TypeInfo createTypeInfoBy(String code) {
        for (FlowableCommentType value : values()) {
            if (value.getCode().equals(code)) {
                return new TypeInfo(value.getCode(), value.getName());
            }
        }
        return TypeInfo.NULL;
    }

    public static TypeInfo createRejectTypeInfo() {
        return new TypeInfo(REJECT.getCode(), REJECT.getName());
    }
}
