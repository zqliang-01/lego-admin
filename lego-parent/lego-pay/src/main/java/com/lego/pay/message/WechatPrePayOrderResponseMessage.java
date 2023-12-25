package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatPrePayOrderResponseMessage extends WechatMerchantMessage {

    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;

    @JacksonXmlProperty(localName = "err_code")
    private String errorCode;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "err_code_des")
    private String errorMessage;

    @JacksonXmlProperty()
    private String sign;

    @JacksonXmlProperty(localName = "result_code")
    private String resultCode;

    @JacksonXmlProperty(localName = "prepay_id")
    private String prepayId;

    @JacksonXmlProperty(localName = "trade_type")
    private String tradeType;

    @JacksonXmlProperty(localName = "code_url")
    private String codeUrl;

    @JacksonXmlProperty(localName = "mweb_url")
    private String mwebUrl;

}
