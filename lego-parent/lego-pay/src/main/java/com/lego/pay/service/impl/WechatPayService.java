package com.lego.pay.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import com.lego.pay.config.WechatPayConfig;
import com.lego.pay.config.WechatUtil;
import com.lego.pay.dto.WechatPreOrderInfo;
import com.lego.pay.feign.FeignWechatMerchantClient;
import com.lego.pay.message.WechatPrePayOrderRequestMessage;
import com.lego.pay.message.WechatPrePayOrderResponseMessage;
import com.lego.pay.service.IWechatPayService;
import com.lego.pay.vo.WechatPreOrderVO;

@Service
public class WechatPayService implements IWechatPayService {

	@Autowired
	private FeignWechatMerchantClient merchantClient;

	@Override
	public WechatPreOrderInfo preOrder(WechatPreOrderVO requestVo) {
    	WechatPrePayOrderRequestMessage requestMessage = WechatUtil.creatWeChatPrePayOrderRequestMessage(requestVo);
        WechatPrePayOrderResponseMessage responseMessage = merchantClient.unifiedorder(requestMessage);

        if (!"SUCCESS".equals(responseMessage.getReturnCode())) {
        	CoreException.check(false, "微信支付下单出错[{0}]", responseMessage.getReturnMsg());
        }
        if (!"SUCCESS".equals(responseMessage.getResultCode())) {
        	CoreException.check(false, "微信支付下单出错[{0}]:[{1}]", responseMessage.getErrorCode(), responseMessage.getErrorMessage());
        }

        String timeStamp = System.currentTimeMillis() + "";
        String nonceStr = StringUtil.getRandomString(8);
        String packagestring = "prepay_id=" + responseMessage.getPrepayId();

        Map<String, String> jsapiParam = WechatUtil.getMapKeyComparator();
        jsapiParam.put("appId", WechatPayConfig.getAppId());
        jsapiParam.put("nonceStr", nonceStr);
        jsapiParam.put("signType", "MD5");
        jsapiParam.put("package", packagestring);
        jsapiParam.put("timeStamp", timeStamp);

        WechatPreOrderInfo responseInfo = new WechatPreOrderInfo();
        String sign = WechatUtil.paySign(jsapiParam, WechatPayConfig.getMchKey());
        responseInfo.setAppId(WechatPayConfig.getAppId());
        responseInfo.setSign(sign);
        responseInfo.setNonceStr(nonceStr);
        responseInfo.setPrePayId(packagestring);
        responseInfo.setTimeStamp(timeStamp);
        responseInfo.setQrCode(responseMessage.getCodeUrl());
        responseInfo.setWebUrl(StringUtil.encodeUrl(responseMessage.getMwebUrl()));
        return responseInfo;
    }

}
