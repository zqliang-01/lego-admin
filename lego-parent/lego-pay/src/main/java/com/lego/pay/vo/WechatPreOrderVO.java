package com.lego.pay.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatPreOrderVO {

    private String orderCode;
    private String requestIp;
    private String openId;
    private String amount; // 分为单位
    private String tradeType;
    private String body;

	public WechatPreOrderVO() { }

	public WechatPreOrderVO(String orderCode, String requestIp, String amount, String tradeType, String body) {
		this.orderCode = orderCode;
		this.requestIp = requestIp;
		this.amount = amount;
		this.tradeType = tradeType;
		this.body = body;
	}

	public WechatPreOrderVO(String orderCode, String requestIp, String openId, String amount, String tradeType, String body) {
		this.orderCode = orderCode;
		this.requestIp = requestIp;
		this.openId = openId;
		this.amount = amount;
		this.tradeType = tradeType;
		this.body = body;
	}

}
