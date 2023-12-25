package com.lego.pay.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "xml")
public class WechatJsSdkRequestMessage {

    @JacksonXmlProperty(localName = "jsapi_ticket")
    private String jsapiTicket;

    @JacksonXmlProperty(localName = "noncestr")
    private String noncestr;

    @JacksonXmlProperty(localName = "timestamp")
    private String timestamp;

    @JacksonXmlProperty(localName = "url")
    private String url;
}
