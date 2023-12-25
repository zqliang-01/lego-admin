package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.entity.SysPermission;
import com.lego.system.entity.simpletype.SysPermissionType;
import com.lego.system.vo.SysPermissionCode;
import com.lego.system.vo.SysPermissionCreateVO;

public class AddSysPermissionAction extends AddAction<SysPermission, ISysPermissionDao> {

	private SysPermissionCreateVO vo;

	public AddSysPermissionAction(String operatorCode, SysPermissionCreateVO vo) {
		super(SysPermissionCode.manage, operatorCode);
		this.vo = vo;
	}

	@Override
	protected void preprocess() {
		BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "菜单编码不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getName()), "菜单名称不能为空！");
		BusinessException.check(StringUtil.isNotBlank(vo.getTypeCode()), "菜单类型不能为空！");

		SysPermission permission = entityDao.findByUnsureCode(vo.getCode());
		BusinessException.check(permission == null, "已存在编码为[{0}]的菜单信息！");
	}

	@Override
	protected SysPermission createTargetEntity() {
		SysPermission permission = new SysPermission(vo.getCode(), vo.getName());
		permission.setType(commonDao.findByCode(SysPermissionType.class, vo.getTypeCode()));
		permission.setParent(entityDao.findByUnsureCode(vo.getParentCode()));
		permission.setSn(vo.getSn());
		return permission;
	}

}
