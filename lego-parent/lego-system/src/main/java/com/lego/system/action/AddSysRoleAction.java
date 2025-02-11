package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysRoleDao;
import com.lego.system.entity.SysRole;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysRoleCreateVO;

public class AddSysRoleAction extends AddAction<SysRole, ISysRoleDao> {

	private SysRoleCreateVO vo;

	public AddSysRoleAction(String operatorCode, SysRoleCreateVO vo) {
		super(SysPermissionCode.manageRole, operatorCode);
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "角色编码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getName()), "角色名称不能为空！");
	}

	@Override
	protected SysRole createTargetEntity() {
		return new SysRole(vo.getCode(), vo.getName());
	}

}
