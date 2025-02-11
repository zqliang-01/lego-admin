package com.lego.system.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysDept;
import com.lego.system.vo.SysDeptModifyVO;
import com.lego.system.vo.SysPermissionCode;

public class ModifySysDeptAction extends ModifyAction<SysDept, ISysDeptDao> {

	private SysDeptModifyVO vo;

	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

	public ModifySysDeptAction(String operatorCode, SysDeptModifyVO vo) {
		super(SysPermissionCode.manageUser, operatorCode, vo.getCode());
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "菜单编码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getName()), "菜单名称不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getLeader()), "部门负责人不能为空！");
	}

	@Override
	protected void doModify(SysDept entity) {
		entity.setName(vo.getName());
		entity.setEnable(vo.isEnable());
		entity.setLeader(employeeDao.findByCode(vo.getLeader()));
		entity.setParent(entityDao.findByUnsureCode(vo.getParentCode()));
		entity.setSerialNumber(vo.getSerialNumber());
	}
}
