package com.lego.pay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lego.pay.config.WechatPayConfig;
import com.lego.pay.message.WechatOrderQueryRequestMessage;
import com.lego.pay.message.WechatOrderQueryResponseMessage;
import com.lego.pay.message.WechatPrePayOrderRequestMessage;
import com.lego.pay.message.WechatPrePayOrderResponseMessage;
import com.lego.pay.message.WechatReconciliationBillRequestMessage;

@FeignClient(name = "wechatMerchantClient", url = WechatPayConfig.MERCHANT_URL)
public interface FeignWechatMerchantClient {

    @PostMapping(value = "/pay/unifiedorder", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    WechatPrePayOrderResponseMessage unifiedorder(@RequestBody WechatPrePayOrderRequestMessage requestMessage);

    @PostMapping(value = "/pay/orderquery", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    WechatOrderQueryResponseMessage orderquery(@RequestBody WechatOrderQueryRequestMessage requestMessage);

    @PostMapping(value = "/pay/downloadbill", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    String downloadbill(@RequestBody WechatReconciliationBillRequestMessage requestMessage);

}
