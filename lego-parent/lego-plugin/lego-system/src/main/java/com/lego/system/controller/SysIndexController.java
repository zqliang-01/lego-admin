package com.lego.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import com.lego.core.data.VersionManager;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.JsonResponse;
import com.lego.core.web.sa.SaTokenCaptchaValidator;
import com.lego.system.dto.SysCaptchaImageInfo;
import com.lego.system.dto.SysLoginInfo;
import com.lego.system.service.ISysEmployeeService;
import com.lego.system.vo.SysLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SaIgnore
@RestController
@RequestMapping("/back-end/sys-index")
public class SysIndexController {

    @Autowired
    private VersionManager versionManager;

    @Autowired
    private ISysEmployeeService employeeService;

    @Autowired
    private SaTokenCaptchaValidator captchaValidator;

    @PostMapping("/login")
    public JsonResponse<SysLoginInfo> login(SysLoginVO vo) {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "验证码不能为空！");
        BusinessException.check(StringUtil.isNotBlank(vo.getToken()), "验证码已失效，请刷新重试！");
        captchaValidator.validCode(vo.getToken(), vo.getCode());
        if (versionManager.needInit()) {
            return JsonResponse.success(new SysLoginInfo());
        }
        return JsonResponse.success(employeeService.login(vo.getUsername(), vo.getPassword()));
    }

    @GetMapping("/logout")
    public JsonResponse<Object> logout() {
        StpUtil.logout();
        return JsonResponse.success();
    }

    @PostMapping("/init")
    public JsonResponse<String> init() {
        return JsonResponse.success(versionManager.execInit());
    }

    @GetMapping("/captchaImage")
    public JsonResponse<SysCaptchaImageInfo> getCode() {
        AbstractCaptcha captcha = CaptchaUtil.createGifCaptcha(130, 50, 4);
        captcha.createCode();
        String code = captcha.getCode();
        String image = captcha.getImageBase64();
        String token = captchaValidator.createToken(code, 60);
        return JsonResponse.success(new SysCaptchaImageInfo(code, image, token));
    }

}
