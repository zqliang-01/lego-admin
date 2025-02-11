package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysEmployeePasswordModifyVO extends VO {

    private static final long serialVersionUID = 1L;

    private String password;
    private String originalPassword;
}
