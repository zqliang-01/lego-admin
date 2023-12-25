package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatOrderRefundResponseMessage extends WechatMerchantMessage {

    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;

    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;

    @JacksonXmlProperty(localName = "device_info")
    private String deviceInfo;

    @JacksonXmlProperty(localName = "sign")
    private String sign;

    @JacksonXmlProperty(localName = "result_code")
    private String resultCode;

    @JacksonXmlProperty(localName = "openid")
    private String openid;

    @JacksonXmlProperty(localName = "is_subscribe")
    private String isSubscribe;

    @JacksonXmlProperty(localName = "trade_type")
    private String tradeType;

    @JacksonXmlProperty(localName = "bank_type")
    private String bankType;

    @JacksonXmlProperty(localName = "total_fee")
    private String totalFee;

    @JacksonXmlProperty(localName = "fee_type")
    private String feeType;

    @JacksonXmlProperty(localName = "transaction_id")
    private String transactionId;

    @JacksonXmlProperty(localName = "out_trade_no")
    private String outTradeNo;

    @JacksonXmlProperty(localName = "attach")
    private String attach;

    @JacksonXmlProperty(localName = "time_end")
    private String timeEnd;

    @JacksonXmlProperty(localName = "out_refund_no")
    private String outRefundNo;

    @JacksonXmlProperty(localName = "refund_id")
    private String refundId;

    @JacksonXmlProperty(localName = "settlement_refund_fee_$n")
    private String settlementRefundFee;

    @JacksonXmlProperty(localName = "refund_fee")
    private String refundFee;

    @JacksonXmlProperty(localName = "trade_state")
    private String tradeState;

    @JacksonXmlProperty(localName = "trade_state_desc")
    private String tradeStateDesc;

    @JacksonXmlProperty(localName = "err_code")
    private String errCode;

    @JacksonXmlProperty(localName = "err_code_des")
    private String errCodeDes;

    @JacksonXmlProperty(localName = "cash_fee")
    private String cashFee;

}
