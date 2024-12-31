package com.lego.system.dto;

import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysDictionaryInfo extends TypeInfo {

    private boolean enable;

    public SysDictionaryInfo(String code, String name) {
        super(code, name);
    }
}
