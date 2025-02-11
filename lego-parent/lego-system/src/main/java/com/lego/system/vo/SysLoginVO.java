package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLoginVO extends VO {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String code;
    private String token;
}
