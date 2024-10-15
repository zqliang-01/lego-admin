package com.lego.mobile.service;

import com.lego.mobile.dto.MobileLoginInfo;

public interface IWechatService {

    String findOpenid(String appCode, String code);

    MobileLoginInfo autoLogin(String openid);
}
