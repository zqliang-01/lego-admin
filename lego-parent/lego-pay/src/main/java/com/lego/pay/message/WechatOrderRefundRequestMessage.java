package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatOrderRefundRequestMessage extends WechatMerchantMessage {

    @JacksonXmlProperty(localName = "appid")
    private String appid;

    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;

    @JacksonXmlProperty(localName = "nonce_str")
    private String nonceStr;

    @JacksonXmlProperty(localName = "op_user_id")
    private String opUserId;

    @JacksonXmlProperty(localName = "out_refund_no")
    private String outRefundNo;

    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    @JacksonXmlProperty(localName = "refund_fee")
    private String refundFee;

    @JacksonXmlProperty(localName = "total_fee")
    private String totalFee;

    @JacksonXmlProperty(localName = "transaction_id")
    private String transactionId;

    @JacksonXmlProperty
    private String sign;
}
