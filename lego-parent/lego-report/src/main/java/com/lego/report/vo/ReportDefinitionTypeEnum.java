package com.lego.report.vo;

import com.lego.core.dto.TypeInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ReportDefinitionTypeEnum {

    TABLE("table", "数据表格"),
    DASH_BROAD("dashBroad", "首页大屏");

    private String code;
    private String name;

    private ReportDefinitionTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<TypeInfo> createTypeInfo() {
        List<TypeInfo> infos = new ArrayList<>();
        for (ReportDefinitionTypeEnum value : values()) {
            infos.add(new TypeInfo(value.code, value.name));
        }
        return infos;
    }

    public static boolean isDashBroad(String code) {
        return DASH_BROAD.code.equals(code);
    }
}
