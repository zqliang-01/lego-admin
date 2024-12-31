package com.lego.core.enums;

import com.lego.core.data.ICommonService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.web.LegoBeanFactory;
import lombok.Getter;

@Getter
public enum PublicTypeEnum {
    EMPLOYEE("员工"),
    DEPT("部门"),
    DICTIONARY("字典");

    private String name;

    private PublicTypeEnum(String name) {
        this.name = name;
    }

    public TypeInfo getValue(String code) {
        if (this == EMPLOYEE) {
            return LegoBeanFactory.getBean(ICommonService.class).findEmployeeBy(code);
        }
        if (this == DEPT) {
            return LegoBeanFactory.getBean(ICommonService.class).findDeptBy(code);
        }
        return LegoBeanFactory.getBean(ICommonService.class).findDictBy(code);
    }
}
