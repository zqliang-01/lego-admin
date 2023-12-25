package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysEmployee;
import com.lego.system.vo.SysEmployeeModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysEmployeeAction extends ModifyAction<SysEmployee, ISysEmployeeDao> {

	private SysEmployeeModifyVO vo;

	private ISysDeptDao edptDao = getDao(ISysDeptDao.class);
	private ISysRoleDao roleDao = getDao(ISysRoleDao.class);

	public ModifySysEmployeeAction(String operator, SysEmployeeModifyVO vo) {
		super(SysPermissionCode.manageUser, operator, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
    	BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "员工编码不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getName()), "员工姓名不能为空！");
    	BusinessException.check(StringUtil.isNotBlank(vo.getDept()), "所属部门不能为空！");
	}

	@Override
	protected void doModify(SysEmployee entity) {
		entity.setName(vo.getName());
		entity.setDept(edptDao.findByCode(vo.getDept()));
		entity.setRoles(roleDao.findByCodes(vo.getRoles()));
		entity.setEnable(vo.isEnable());
	}
}