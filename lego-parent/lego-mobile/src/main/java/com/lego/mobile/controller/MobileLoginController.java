package com.lego.mobile.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.mobile.action.MobileLogoutAction;
import com.lego.mobile.dto.MobileLoginInfo;
import com.lego.mobile.service.IWechatMpService;
import com.lego.mobile.service.IWechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SaIgnore
@RestController
@RequestMapping("/back-end/mobile-login")
public class MobileLoginController {

    @Autowired
    private IWechatMpService wechatMpService;

    @Autowired
    private IWechatService wechatService;

    @GetMapping("/wechat-mp/{code}")
    public JsonResponse<MobileLoginInfo> mpWxLogin(@RequestHeader("appCode") String appCode, @PathVariable String code) {
        String openid = wechatMpService.findOpenid(appCode, code);
        return JsonResponse.success(wechatMpService.autoLogin(openid));
    }

    @GetMapping("/wechat/{code}")
    public JsonResponse<MobileLoginInfo> wxLogin(@RequestHeader("appCode") String appCode, @PathVariable String code) {
        String openid = wechatService.findOpenid(appCode, code);
        return JsonResponse.success(wechatService.autoLogin(openid));
    }

    @GetMapping("/logout")
    public JsonResponse<Object> logout() {
        if (StpUtil.isLogin()) {
            String token = StpUtil.getTokenValue();
            String operatorCode = StpUtil.getLoginIdAsString();
            new MobileLogoutAction(operatorCode, token).run();
        }
        return JsonResponse.success();
    }

}
