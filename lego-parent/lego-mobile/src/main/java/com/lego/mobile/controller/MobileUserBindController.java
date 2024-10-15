package com.lego.mobile.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.mobile.service.IMobileUserBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/mobile-user-bind")
public class MobileUserBindController extends BaseController {

    @Autowired
    private IMobileUserBindService bindService;

    @PostMapping("/update/{openid}")
    public JsonResponse<Object> update(@RequestHeader("appCode") String appCode, @PathVariable String openid) {
        String token = StpUtil.getTokenValue();
        bindService.update(getLoginCode(), appCode, openid, token);
        return JsonResponse.success();
    }

    @PostMapping("/remove/{openid}")
    public JsonResponse<Object> remove(@RequestHeader("appCode") String appCode, @PathVariable String openid) {
        String token = StpUtil.getTokenValue();
        bindService.update(getLoginCode(), appCode, openid, token);
        return JsonResponse.success();
    }

}
