package com.lego.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictionaryInfo extends TypeInfo {

    private boolean enable;

    public DictionaryInfo(String code, String name) {
        super(code, name);
    }
}
