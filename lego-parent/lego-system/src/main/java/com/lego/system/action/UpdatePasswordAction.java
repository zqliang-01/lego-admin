package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysPermissionCode;

public class UpdatePasswordAction extends ModifyAction<SysEmployee, ISysEmployeeDao> {

	private String oldPassword;
	private String newPassword;

	public UpdatePasswordAction(String operatorCode, String oldPassword, String newPassword) {
		super(SysPermissionCode.manage, operatorCode, operatorCode);
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(oldPassword), "旧密码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(newPassword), "新密码不能为空！");
		BusinessException.check(StringUtil.equals(oldPassword, newPassword), "新旧密码不能相同！");
		BusinessException.check(targetEntity.checkPassword(oldPassword), "旧密码校验不通过！");
	}

	@Override
	protected void doModify(SysEmployee entity) {
		entity.resetPassword(newPassword);
	}
}
