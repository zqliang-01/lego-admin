package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatMicroPayRequestMessage extends WechatMerchantMessage {

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "auth_code")
    public String authCode;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "body")
    private String body;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "spbill_create_ip")
    public String spbillCreateIp;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "total_fee")
    private String totalFee;

    @JacksonXmlCData
    @JacksonXmlProperty
    private String sign;

}
