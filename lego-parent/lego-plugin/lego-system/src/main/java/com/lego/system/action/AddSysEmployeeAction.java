package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysEmployeeCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysEmployeeAction extends AddAction<SysEmployee, ISysEmployeeDao> {

	private SysEmployeeCreateVO vo;

	private ISysDeptDao deptDao = getDao(ISysDeptDao.class);
	private ISysRoleDao roleDao = getDao(ISysRoleDao.class);

	public AddSysEmployeeAction(String operator, SysEmployeeCreateVO vo) {
		super(SysPermissionCode.manageUser, operator);
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "员工姓名不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getDept()), "所属部门不能为空！");
	}

	@Override
	protected SysEmployee createTargetEntity() {
		SysEmployee employee = new SysEmployee(vo.getName());
		employee.setEnable(vo.isEnable());
    	employee.setDept(deptDao.findByCode(vo.getDept()));
    	employee.setRoles(roleDao.findByCodes(vo.getRoles()));
    	employee.resetPassword(employee.getCode());
		return employee;
	}

}
