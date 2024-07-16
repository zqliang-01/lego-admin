package com.lego.flowable.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowableTaskLogType {

    REJECT("reject", "拒绝");

    private final String code;
    private final String name;

}
