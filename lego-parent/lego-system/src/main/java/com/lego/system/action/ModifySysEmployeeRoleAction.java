package com.lego.system.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.enums.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysEmployeeRoleModifyVO;
import com.lego.system.vo.SysPermissionCode;

import java.util.List;

public class ModifySysEmployeeRoleAction extends MaintainAction {

	private SysEmployeeRoleModifyVO vo;

	private ISysRoleDao roleDao = getDao(ISysRoleDao.class);
	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

	public ModifySysEmployeeRoleAction(String operator, SysEmployeeRoleModifyVO vo) {
		super(SysPermissionCode.manageUser, operator);
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(vo.getRoleCode()), "角色不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getEmployeeCodes()), "用户不能为空！");
	}

	@Override
	protected void doRun() {
		SysRole role = roleDao.findByCode(vo.getRoleCode());
		List<SysEmployee> employees = employeeDao.findByCodes(vo.getEmployeeCodes());
		for (SysEmployee employee : employees) {
			if ("add".equals(vo.getAction())) {
				employee.addRole(role);
			} else {
				employee.getRoles().remove(role);
			}
		}
		employeeDao.saveAll(employees);
	}

	@Override
	protected ActionType getActionType() {
		return ActionType.MODIFY;
	}

	@Override
	protected String getEntityName() {
		return "批量更新用户角色";
	}

}