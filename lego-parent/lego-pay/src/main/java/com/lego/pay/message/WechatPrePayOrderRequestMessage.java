package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatPrePayOrderRequestMessage extends WechatMerchantMessage {

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "attach")
    private String attach;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "body")
    private String body;

    @JacksonXmlProperty(localName = "notify_url")
    private String notifyUrl;

    @JacksonXmlProperty(localName = "openid")
    private String openid;

    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "scene_info")
    private String sceneInfo;

    @JacksonXmlProperty(localName = "spbill_create_ip")
    public String spbillCreateIp;

    @JacksonXmlProperty(localName = "total_fee")
    private String totalFee;

    @JacksonXmlProperty(localName = "trade_type")
    private String tradeType;

    private String sign;
}
