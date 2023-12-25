package com.lego.system.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lego.core.util.StringUtil;
import com.lego.system.mapper.SysPermissionMapper;
import com.lego.system.mapper.SysRoleMapper;

import cn.dev33.satoken.stp.StpInterface;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

	@Autowired
	private SysPermissionMapper permissionMapper;

	@Autowired
	private SysRoleMapper roleMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String employeeCode = StringUtil.toString(loginId);
		return permissionMapper.selectCodesByEmployee(employeeCode);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String employeeCode = StringUtil.toString(loginId);
		List<String> roleCodes = roleMapper.selectCodesByEmployee(employeeCode);
		return roleCodes;
    }

}
