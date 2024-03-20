package com.lego.flowable.vo;

import com.lego.core.dto.TypeInfo;
import com.lego.core.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessStatus {

    RUNNING("running", "进行中"),
    TERMINATED("terminated", "已终止"),
    COMPLETED("completed", "已完成"),
    CANCELED("canceled", "已取消");

    private final String code;
    private final String name;

    public static String getName(String code) {
        ProcessStatus status = get(code);
        if (status != null) {
            return status.getName();
        }
        return null;
    }

    public static TypeInfo getTypeInfo(String code) {
        ProcessStatus status = get(code);
        if (status == null) {
            return TypeInfo.NULL;
        }
        return new TypeInfo(status.getCode(), status.getName());
    }

    public static ProcessStatus get(String code) {
        if (StringUtil.isNotBlank(code)) {
            for (ProcessStatus value : values()) {
                if (StringUtil.equals(code.toLowerCase(), value.getCode())) {
                    return value;
                }
            }
        }
        return null;
    }

    public boolean isRunning() {
        return RUNNING.getCode().equals(code);
    }
}
