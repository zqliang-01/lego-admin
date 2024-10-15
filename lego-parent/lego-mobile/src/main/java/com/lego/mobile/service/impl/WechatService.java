package com.lego.mobile.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.exception.BusinessException;
import com.lego.mobile.action.MobileAutoLoginAction;
import com.lego.mobile.dao.IMobileAppConfigDao;
import com.lego.mobile.dao.IMobileUserBindDao;
import com.lego.mobile.dto.MobileLoginInfo;
import com.lego.mobile.dto.MobileWechatOpenidInfo;
import com.lego.mobile.entity.MobileAppConfig;
import com.lego.mobile.entity.MobileUserBind;
import com.lego.mobile.feign.FeignWechatClient;
import com.lego.mobile.service.IWechatService;
import com.lego.mobile.vo.MobileAppTypeCode;
import com.lego.mobile.vo.WechatOpenidVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatService implements IWechatService {

    @Autowired
    private FeignWechatClient client;

    @Autowired
    private IMobileAppConfigDao appConfigDao;

    @Autowired
    private IMobileUserBindDao userBindDao;

    @Override
    public String findOpenid(String appCode, String code) {
        MobileAppConfig appConfig = appConfigDao.findByUnsureCode(code);
        BusinessException.check(appConfig != null, "[{0}]信息未配置，授权登陆失败！", appCode);
        WechatOpenidVO vo = new WechatOpenidVO(appConfig.getAppid(), appConfig.getSecret(), code);
        MobileWechatOpenidInfo response = client.code2session(vo);
        BusinessException.check(response.isSuccess(), "获取用户小程序标识失败[{0}]", response.getErrmsg());
        return response.getOpenid();
    }

    @Override
    public MobileLoginInfo autoLogin(String openid) {
        MobileUserBind userBind = userBindDao.findBy(openid, MobileAppTypeCode.WECHAT);
        if (userBind == null || userBind.isExpired()) {
            return new MobileLoginInfo(openid);
        }
        new MobileAutoLoginAction(userBind.getEmployeeCode(), userBind).run();
        return new MobileLoginInfo(userBind.getEmployeeCode(), StpUtil.getTokenValue());
    }
}
