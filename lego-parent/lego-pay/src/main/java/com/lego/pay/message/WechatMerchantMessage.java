package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatMerchantMessage {

    @JacksonXmlProperty(localName = "appid")
    private String appid;

    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;

    @JacksonXmlProperty(localName = "nonce_str")
    private String nonceStr;

}
