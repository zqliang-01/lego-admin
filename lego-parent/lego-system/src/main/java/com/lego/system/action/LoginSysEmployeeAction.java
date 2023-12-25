package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysPermissionCode;

import cn.dev33.satoken.stp.StpUtil;

public class LoginSysEmployeeAction extends MaintainAction {

	private String code;
	private String password;

	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

	public LoginSysEmployeeAction(String code, String password) {
		super(SysPermissionCode.manage, code);
		this.code = code;
		this.password = password;
	}

	@Override
	protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(code), "登录账号不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(password), "登录密码不能为空！");

    	SysEmployee employee = employeeDao.findByUnsureCode(code);
    	BusinessException.check(employee != null, "账号[{0}]不存在！", code);
    	BusinessException.check(employee.isEnable(), "账号[{0}]未激活！", code);

		boolean result = employee.checkPassword(password);
    	BusinessException.check(result, "密码错误！");
	}

	@Override
	protected void doRun() {
        StpUtil.login(code);
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.LOGIN;
	}

}
