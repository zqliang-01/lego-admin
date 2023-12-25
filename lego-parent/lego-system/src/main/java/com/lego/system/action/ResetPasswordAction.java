package com.lego.system.action;

import java.util.List;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysPermissionCode;

public class ResetPasswordAction extends MaintainAction {

	private List<String> codes;

	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

	public ResetPasswordAction(String operatorCode, List<String> codes) {
		super(SysPermissionCode.manage, operatorCode);
		this.codes = codes;
	}

	@Override
	protected void doRun() {
		List<SysEmployee> employees = employeeDao.findByCodes(codes);
		for (SysEmployee employee : employees) {
			employee.resetPassword(employee.getCode());
		}
		employeeDao.saveAll(employees);
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.MODIFY;
	}

}
