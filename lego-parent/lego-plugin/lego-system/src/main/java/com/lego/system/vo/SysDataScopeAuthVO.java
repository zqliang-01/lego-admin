package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysDataScopeAuthVO extends VO {

    private static final long serialVersionUID = 1L;

    private String roleCode;
    private int dataScope;
}
