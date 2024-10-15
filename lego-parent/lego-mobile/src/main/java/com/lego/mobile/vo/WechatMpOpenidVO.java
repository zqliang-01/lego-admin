package com.lego.mobile.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatMpOpenidVO extends VO {

    private String appid;
    private String secret;
    private String js_code;
    private String grant_type = "authorization_code";

    public WechatMpOpenidVO(String appid, String secret, String code) {
        this.appid = appid;
        this.secret = secret;
        this.js_code = code;
    }
}
