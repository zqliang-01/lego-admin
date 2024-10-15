package com.lego.mobile.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileUserBindCreateVO extends VO {

    private String openid;
    private String appCode;
}
