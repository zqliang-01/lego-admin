package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLoginInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String token;
    private boolean needInit;

    public SysLoginInfo() {
        this.needInit = true;
    }

    public SysLoginInfo(String code, String token) {
        this.code = code;
        this.token = token;
    }

}
