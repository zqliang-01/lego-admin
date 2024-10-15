package com.lego.mobile.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileWechatInfo extends DTO {

    private int errcode;
    private String errmsg;

    public boolean isSuccess() {
        return errcode == 0;
    }
}
