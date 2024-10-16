package com.lego.mobile.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileWechatMpOpenidInfo extends MobileWechatInfo {

    private String unionid;
    private String openid;
    private String session_key;
}
