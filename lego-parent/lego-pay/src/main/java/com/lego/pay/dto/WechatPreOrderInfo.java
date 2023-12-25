package com.lego.pay.dto;

import com.lego.core.dto.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatPreOrderInfo extends DTO {

	private static final long serialVersionUID = 1L;

	private String prePayId;
    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String sign;
    private String qrCode;
    private String webUrl;
}
