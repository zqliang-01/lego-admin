package com.lego.core.web.sa;

import cn.dev33.satoken.stp.StpInterface;
import com.lego.core.data.ICommonService;
import com.lego.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口扩展，权限类操作频繁，建议通过缓存读取权限信息
 */
@Component
public class SaAuthImpl implements StpInterface {

    @Autowired
    private ICommonService commonService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String employeeCode = StringUtil.toString(loginId);
        return commonService.findPermissionCodesBy(employeeCode);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String employeeCode = StringUtil.toString(loginId);
        return commonService.findRoleCodesBy(employeeCode);
    }

}
