package com.lego.mobile.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileLoginInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String token;
    private String openid;
    private boolean needLogin;

    public MobileLoginInfo(String openid) {
        this.needLogin = true;
        this.openid = openid;
    }

    public MobileLoginInfo(String code, String token) {
        this.code = code;
        this.token = token;
    }

}
