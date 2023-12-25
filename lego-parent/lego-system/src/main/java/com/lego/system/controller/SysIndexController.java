package com.lego.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.system.dto.SysCaptchaImageInfo;
import com.lego.system.dto.SysLoginInfo;
import com.lego.system.service.ISysEmployeeService;
import com.lego.system.vo.SysLoginVO;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;

@SaIgnore
@RestController
@RequestMapping("/back-end/sys-index")
public class SysIndexController {

	@Autowired
	private ISysEmployeeService employeeService;

	@Autowired
	private StpLogic stpLogic;

    @PostMapping("/login")
    public JsonResponse<SysLoginInfo> login(SysLoginVO vo) {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "验证码不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getToken()), "验证码已失效，请刷新重试！");

    	String code = StringUtil.toString(stpLogic.getLoginIdByToken(vo.getToken()));
    	BusinessException.check(StringUtil.isNotBlank(code), "验证码已失效，请刷新重试！");
    	BusinessException.check(StringUtil.equals(code, vo.getCode()), "验证码不一致，请刷新重试！");

        return JsonResponse.success(employeeService.login(vo.getUsername(), vo.getPassword()));
    }

    @GetMapping("/logout")
    public JsonResponse<Object> logout(String code, String password) {
    	StpUtil.logout();
        return JsonResponse.success();
    }

    @GetMapping("/captchaImage")
    public JsonResponse<SysCaptchaImageInfo> getCode() {
        AbstractCaptcha captcha = CaptchaUtil.createGifCaptcha(130, 50, 4);
        captcha.createCode();
        String code = captcha.getCode();
        String image = captcha.getImageBase64();
        String token = stpLogic.createTokenValue(code, "captchaImage", 60, null);
        return JsonResponse.success(new SysCaptchaImageInfo(code, image, token));
    }

}
