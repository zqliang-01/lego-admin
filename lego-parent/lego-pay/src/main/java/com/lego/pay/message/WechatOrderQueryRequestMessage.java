package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatOrderQueryRequestMessage extends WechatMerchantMessage {

    @JacksonXmlProperty(localName = "transaction_id")
    private String transactionId;

    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    @JacksonXmlProperty
    private String sign;

}
