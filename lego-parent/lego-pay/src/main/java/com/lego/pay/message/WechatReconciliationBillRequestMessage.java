package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatReconciliationBillRequestMessage {

    @JacksonXmlProperty(localName = "appid")
    private String appid;

    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;

    @JacksonXmlProperty(localName = "nonce_str")
    private String nonceStr;

    @JacksonXmlProperty(localName = "bill_date")
    private String billDate;

    @JacksonXmlProperty(localName = "bill_type")
    private String billType;

    @JacksonXmlProperty(localName = "sign")
    private String sign;

}
