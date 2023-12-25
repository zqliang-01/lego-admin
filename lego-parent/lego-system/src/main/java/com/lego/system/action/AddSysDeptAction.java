package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysDeptDao;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysDept;
import com.lego.system.vo.SysDeptCreateVO;
import com.lego.system.vo.SysPermissionCode;

public class AddSysDeptAction extends AddAction<SysDept, ISysDeptDao> {

	private SysDeptCreateVO vo;

	private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

	public AddSysDeptAction(String operatorCode, SysDeptCreateVO vo) {
		super(SysPermissionCode.manageUser, operatorCode);
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "菜单编码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getName()), "菜单名称不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getLeader()), "部门负责人不能为空！");
	}

	@Override
	protected SysDept createTargetEntity() {
		SysDept dept = new SysDept(vo.getCode(), vo.getName());
		dept.setLeader(employeeDao.findByCode(vo.getLeader()));
		dept.setParent(entityDao.findByUnsureCode(vo.getParentCode()));
		dept.setSerialNumber(vo.getSerialNumber());
		return dept;
	}
}
