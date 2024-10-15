package com.lego.mobile.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatOpenidVO extends VO {

    private String appid;
    private String secret;
    private String code;
    private String grant_type = "authorization_code";

    public WechatOpenidVO(String appid, String secret, String code) {
        this.appid = appid;
        this.secret = secret;
        this.code = code;
    }
}
