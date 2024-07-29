package com.lego.core.web.sa;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpLogic;
import com.lego.core.exception.BusinessException;
import com.lego.core.exception.CoreException;
import com.lego.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaTokenCaptchaValidator {

    @Autowired(required = false)
    private SaTokenDao tokenDao;

    @Autowired(required = false)
    private StpLogic stpLogic;

    public String createToken(String code, long timeout) {
        if (tokenDao instanceof SaTokenDaoRedisImpl) {
            String uuid = StringUtil.getUUID();
            tokenDao.set(uuid, code, timeout);
            return uuid;
        }
        CoreException.check(stpLogic != null, "StpLogic未实例化，验证码生成失败！");
        return stpLogic.createTokenValue(code, "captchaImage", timeout, null);
    }

    public void validCode(String token, String code) {
        if (tokenDao instanceof SaTokenDaoRedisImpl) {
            String originalCode = tokenDao.get(token);
            BusinessException.check(StringUtil.isNotBlank(originalCode), "验证码已失效，请刷新重试！");
            BusinessException.check(StringUtil.equals(originalCode.toLowerCase(), code.toLowerCase()), "验证码不一致，请刷新重试！");
            tokenDao.delete(token);
            return;
        }
        CoreException.check(stpLogic != null, "StpLogic未实例化，验证码校验失败！");
        String originalCode = StringUtil.toString(stpLogic.getLoginIdByToken(token));
        BusinessException.check(StringUtil.isNotBlank(originalCode), "验证码已失效，请刷新重试！");
        BusinessException.check(StringUtil.equals(originalCode.toLowerCase(), code.toLowerCase()), "验证码不一致，请刷新重试！");
    }
}
