package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysDictionaryVO extends VO {

    private String typeCode;
    private String code;
    private String name;
    private int sn;
    private boolean enable;
}
