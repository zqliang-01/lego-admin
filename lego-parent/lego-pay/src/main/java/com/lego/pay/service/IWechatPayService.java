package com.lego.pay.service;

import com.lego.pay.dto.WechatPreOrderInfo;
import com.lego.pay.vo.WechatPreOrderVO;

public interface IWechatPayService {

	WechatPreOrderInfo preOrder(WechatPreOrderVO requestVo);
}
