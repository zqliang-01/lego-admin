package com.lego.core.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictionaryVO extends VO {

    private String typeCode;
    private String code;
    private String name;
    private int sn;
    private boolean enable;
}
